/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.page;

import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import edu.stratford.rbandlar8760.rssfeeds.model.CategoryModel;
import edu.stratford.rbandlar8760.rssfeeds.model.ModelException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RamyaNari
 */
public class JsonCategory extends JsonPage{

    @Override
    public void jsonGet() {
        final JSONObject jobj = new JSONObject();
        final JSONArray items = new JSONArray();


        try {
            CategoryModel im = new CategoryModel();
            final List<Category> catl = im.list();

            int i = 0;
            for (final Category cat : catl) {
                final JSONObject contents = new JSONObject();

                contents.put("cat_name", cat.getCategoryName());
                contents.put("id", i++);

                items.add(contents);
            }
            catl.clear();

            jobj.put("items", items);
            jobj.put("label", "cat_name");
            jobj.put("identifier", "id");

            setJson(jobj);
        } catch (ModelException me) {
            System.err.println(me.getMessage());

        }
    }

    @Override
    public void jsonPost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
