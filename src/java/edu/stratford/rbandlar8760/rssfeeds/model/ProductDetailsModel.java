/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import edu.stratford.rbandlar8760.rssfeeds.db.MappingDetails;
import edu.stratford.rbandlar8760.rssfeeds.db.ProductDetails;
import edu.stratford.rbandlar8760.rssfeeds.db.Website;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;

/**
 *
 * @author RamyaNari
 */
public class ProductDetailsModel extends BaseModel {


      public List<ProductDetails> list() throws ModelException
    {
        try
        {
            final SelectQuery query = new SelectQuery( ProductDetails.class );
           // query.addOrdering("PRODUCT_DETAILS_ID_PK_COLUMN", SortOrder.DESCENDING);
             query.addOrdering("date", SortOrder.DESCENDING);
            @SuppressWarnings("unchecked")
            final List<ProductDetails> art = dataContext.performQuery( query );
            if (art.isEmpty())
            {
                throw new ModelException( "Could not lookup product details" );
            }
            return art;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( "Database error trying to list category list" );
        }
    }

    public ProductDetails lookup(Website w, String title) throws ModelException {
        try {
            Expression qualifier = Expression.fromString("toMappingDetails.toWebsite = $w and title = $title");
            final HashMap<String, Object> map = new HashMap<String, Object>(2);
            map.put("w", w);
            map.put("title", title);
            qualifier = qualifier.expWithParameters(map);

            final SelectQuery query = new SelectQuery(ProductDetails.class, qualifier);
            @SuppressWarnings("unchecked")
            final List<ProductDetails> mapD = dataContext.performQuery(query);
            if (mapD.isEmpty()) {
                return null;
            } else {
                return mapD.get(0);
            }
        } catch (CayenneRuntimeException ce) {
            throw new ModelException(ce.getMessage());
        }


    }

    public ProductDetails create(MappingDetails md, String title, Date d, String description, String link) throws ModelException {

        try {
            final ProductDetails pd = dataContext.newObject(ProductDetails.class);

            pd.setToMappingDetails(md);
            pd.setDate(d);
            pd.setTitle(title);
            pd.setLink(link);
            pd.setDescription(description);
            dataContext.commitChanges();
            return pd;
        } catch (CayenneRuntimeException ce) {
            ce.printStackTrace();
            throw new ModelException(ce.getMessage());
        }
    }

    public List<ProductDetails> searchTitle( List<String> searchStrings ) throws ModelException{

        String searchStr = "";

        for( int i =0; i< searchStrings.size(); i++ ){
            String s = searchStrings.get(i);
            
            searchStr = searchStr + "title like '%"+s+"%'";

            if( i != searchStrings.size() - 1 ){

                searchStr = searchStr+" and ";
            }
        }
        if( searchStr.length() == 0 ){
            List<ProductDetails> pdl = list();
            return pdl;
        }else{
            System.out.println("\n\nsearchString:"+searchStr+"\n\n");
            Expression qualifier = Expression.fromString(searchStr);
            final SelectQuery query = new SelectQuery(ProductDetails.class, qualifier);
            query.addOrdering("date", SortOrder.DESCENDING);
          //  query.addOrdering("PRODUCT_DETAILS_ID_PK_COLUMN", SortOrder.DESCENDING);
            @SuppressWarnings("unchecked")
            final List<ProductDetails> mapD = dataContext.performQuery(query);
            return mapD;
        }
    }


    public List<ProductDetails> searchTitle( List<String> searchStrings , Category cat  ) throws ModelException{

        String searchStr = "";
final HashMap<String, Object> map = new HashMap<String, Object>();
        for( int i =0; i< searchStrings.size(); i++ ){
            String s = searchStrings.get(i);

            searchStr = searchStr + "title like '%"+s+"%'";

            if( i != searchStrings.size() - 1 ){

                searchStr = searchStr+" and ";
            }
        }

        if(cat != null){
            searchStr = searchStr+ " and toMappingDetails.toCategory = $cat";
            map.put("cat", cat);
        }

        if( searchStr.length() == 0 ){
            List<ProductDetails> pdl = list();
            return pdl;
        }else{

            System.out.println("\n\nsearchString:"+searchStr+"\n\n");
            Expression qualifier = Expression.fromString(searchStr);
             qualifier = qualifier.expWithParameters( map );
            final SelectQuery query = new SelectQuery(ProductDetails.class, qualifier);
            query.addOrdering("date", SortOrder.DESCENDING);
          //  query.addOrdering("PRODUCT_DETAILS_ID_PK_COLUMN", SortOrder.DESCENDING);
            @SuppressWarnings("unchecked")
            final List<ProductDetails> mapD = dataContext.performQuery(query);
            return mapD;
        }
    }
}
