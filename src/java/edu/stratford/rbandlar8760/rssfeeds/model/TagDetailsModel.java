/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Information;
import edu.stratford.rbandlar8760.rssfeeds.db.TagDetails;
import edu.stratford.rbandlar8760.rssfeeds.db.Website;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author RamyaNari
 */
public class TagDetailsModel extends BaseModel{

   public List<TagDetails> listTagDetailsForWebsite( Website w ) throws ModelException
    {
        try
        {
            Expression qualifier = Expression.fromString( "toWebsite = $website" );
            final HashMap<String, Object> map = new HashMap<String, Object>( 2 );
            map.put( "website", w );
            qualifier = qualifier.expWithParameters( map );

            final SelectQuery query = new SelectQuery( TagDetails.class, qualifier );
            @SuppressWarnings("unchecked")
            final List<TagDetails> td = dataContext.performQuery( query );
            if (td.isEmpty())
            {
                throw new ModelException( "Could not list Tag Details" );
            }
            return td;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( ce.getMessage() );
        }
    }




  public TagDetails create( Website w, Information i , String tag ) throws ModelException{

      if ( tag == null || tag.length() == 0 )
          throw new IllegalArgumentException("Invalid tag name");

      if ( w == null )
          throw new IllegalArgumentException("Invalid website");

      if ( i == null )
          throw new IllegalArgumentException("Invalid information");
      
      try{
          final TagDetails td = dataContext.newObject(TagDetails.class);
            td.setToWebsite(w);
            td.setTagname(tag);
            td.setToInformation(i);
            
          dataContext.commitChanges();
          return td;
      }catch (CayenneRuntimeException ce)
      {
          ce.printStackTrace();
          dataContext.rollbackChanges();
          throw new ModelException("Could not add Tag Details");
      }
  }
}
