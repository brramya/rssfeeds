/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Website;
import edu.stratford.rbandlar8760.rssfeeds.db.WebsiteUrl;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author RamyaNari
 */
public class WebsiteUrlModel extends BaseModel {

    public List<WebsiteUrl> list() throws ModelException {
        try {
            final SelectQuery query = new SelectQuery(WebsiteUrl.class);
            @SuppressWarnings("unchecked")
            final List<WebsiteUrl> art = dataContext.performQuery(query);
            if (art.isEmpty()) {
                throw new ModelException("Could not lookup website url");
            }
            return art;
        } catch (CayenneRuntimeException ce) {
            ce.printStackTrace();
            throw new ModelException("Database error trying to list website url list");
        }
    }

    public WebsiteUrl lookupUrl(String url) throws ModelException {
        try {
            Expression qualifier = Expression.fromString("url = $url");
            final HashMap<String, Object> map = new HashMap<String, Object>(1);
            map.put("url", url);
            qualifier = qualifier.expWithParameters(map);

            final SelectQuery query = new SelectQuery(WebsiteUrl.class, qualifier);
            @SuppressWarnings("unchecked")
            final List<WebsiteUrl> wu = dataContext.performQuery(query);
            if (!wu.isEmpty()) {
                return wu.get(0);
            }
            return null;
        } catch (CayenneRuntimeException ce) {
            throw new ModelException(ce.getMessage());
        }
    }

    public List<WebsiteUrl> listUrlByWebsite(Website w) throws ModelException {

        if (w == null) {
            throw new IllegalArgumentException("Invalid website");
        }

        try {
            Expression qualifier = Expression.fromString("toWebsite = $web");
            final HashMap<String, Object> map = new HashMap<String, Object>(1);
            map.put("web", w);
            qualifier = qualifier.expWithParameters(map);

            final SelectQuery query = new SelectQuery(WebsiteUrl.class, qualifier);
            @SuppressWarnings("unchecked")
            final List<WebsiteUrl> wu = dataContext.performQuery(query);
            return wu;
        } catch (CayenneRuntimeException ce) {
            throw new ModelException(ce.getMessage());
        }

    }

    public WebsiteUrl createWebsiteUrl(Website w, String url) throws ModelException {

    
        if (url == null || url.length() == 0) {
            throw new IllegalArgumentException("Invalid url name");
        }

        if (w == null) {
            throw new IllegalArgumentException("Invalid website");
        }
        try {
            final WebsiteUrl wu = dataContext.newObject(WebsiteUrl.class);
            wu.setUrl(url);
            wu.setToWebsite(w);
            dataContext.commitChanges();
            return wu;
        } catch (CayenneRuntimeException ce) {
            ce.printStackTrace();
            dataContext.rollbackChanges();
            throw new ModelException(ce.getMessage());
        }
    }

    public WebsiteUrl checkAndCreateWebsiteUrl(Website w, String url) throws ModelException {


        System.out.println("\n\n\nIn Checck and create\n\n\n");
        if (url == null || url.length() == 0) {
            throw new IllegalArgumentException("Invalid url name");
        }

        if (w == null) {
            throw new IllegalArgumentException("Invalid website");
        }
        try {
            WebsiteUrl wu = lookupUrl(url);
            if (wu == null) {
                wu = createWebsiteUrl(w, url);
            }
            return wu;
        } catch (CayenneRuntimeException ce) {
            ce.printStackTrace();
            dataContext.rollbackChanges();
            throw new ModelException("Could not add Category");
        }
    }
}
