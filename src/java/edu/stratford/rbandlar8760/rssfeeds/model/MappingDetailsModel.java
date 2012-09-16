/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import edu.stratford.rbandlar8760.rssfeeds.db.MappingDetails;
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
public class MappingDetailsModel extends BaseModel {

    public MappingDetails lookUpMappingDetails(Website w, Category c) throws ModelException {


        try {
            Expression qualifier = Expression.fromString("toCategory = $cat and toWebsite = $web");
            final HashMap<String, Object> map = new HashMap<String, Object>(2);
            map.put("cat", c);
            map.put("web", w);
            qualifier = qualifier.expWithParameters(map);

            final SelectQuery query = new SelectQuery(MappingDetails.class, qualifier);
            @SuppressWarnings("unchecked")
            final List<MappingDetails> mapD = dataContext.performQuery(query);
            if (mapD.isEmpty()) {
                return null;
            } else {
                return mapD.get(0);
            }
        } catch (CayenneRuntimeException ce) {
            throw new ModelException(ce.getMessage());
        }

    }

    public MappingDetails lookUpAndCreateMappingDetails(Website w, Category c) throws ModelException {


        try {
            Expression qualifier = Expression.fromString("toCategory=$cat and toWebsite = $web");
            final HashMap<String, Object> map = new HashMap<String, Object>(2);
            map.put("cat", c);
            map.put("web", w);
            qualifier = qualifier.expWithParameters(map);

            final SelectQuery query = new SelectQuery(MappingDetails.class, qualifier);
            @SuppressWarnings("unchecked")
            final List<MappingDetails> mapD = dataContext.performQuery(query);
            if (mapD.isEmpty()) {
                return create(w, c);
            } else {
                return mapD.get(0);
            }
        } catch (CayenneRuntimeException ce) {
            ce.printStackTrace();
            throw new ModelException(ce.getMessage());

        }

    }

    public MappingDetails create(Website w, Category c) throws ModelException {
        try {
            final MappingDetails md = dataContext.newObject(MappingDetails.class);

            System.out.println("\n\n\nCateogry:" + c.getCategoryName() + "\n\n\n");
            md.setToCategory(c);
            md.setToWebsite(w);
            dataContext.commitChanges();
            return md;
        } catch (CayenneRuntimeException ce) {
            throw new ModelException(ce.getMessage());
        }
    }
}
