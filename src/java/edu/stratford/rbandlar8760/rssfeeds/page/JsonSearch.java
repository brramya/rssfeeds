/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds.page;

import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import edu.stratford.rbandlar8760.rssfeeds.db.ProductDetails;
import edu.stratford.rbandlar8760.rssfeeds.model.CategoryModel;
import edu.stratford.rbandlar8760.rssfeeds.model.ModelException;
import edu.stratford.rbandlar8760.rssfeeds.model.ProductDetailsModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RamyaNari
 */
public class JsonSearch extends JsonPage {

    @Override
    public void jsonGet() {
        try {
            final String cat = this.getContext().getRequestParameter("cat");
            final String searchString = this.getContext().getRequestParameter("searchStr");
            Category category = null;
            String[] ss = null;
            if (searchString != null) {
                ss = searchString.split("/");
            } else {
                ss = new String[0];
            }
            List<String> ssl = Arrays.asList(ss);
            ProductDetailsModel pdm = new ProductDetailsModel();

            if (cat != null && cat.length() > 0) {
                category = new CategoryModel().lookUpCategory(cat);
            }
            List<ProductDetails> pdl = pdm.searchTitle(ssl, category);
            final JSONObject jobj = new JSONObject();
            final JSONArray items = new JSONArray();

            int i = 0;
            for (final ProductDetails pd : pdl) {
                final JSONObject contents = new JSONObject();

                contents.put("title", pd.getTitle());
                contents.put("description", pd.getDescription());
                if( pd.getDate() == null )
                    contents.put("date", "");
                else{
                   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = formatter.format(pd.getDate());
                    contents.put("date", formattedDate );
                }
                contents.put("link","<a style=\"color: blue\" href=\""+pd.getLink()+"\" target=\"_blank\"> Original Link </a>");
                contents.put("id", i++);

                items.add(contents);
            }
            pdl.clear();

            jobj.put("items", items);
            jobj.put("label", "title");
            jobj.put("identifier", "id");

            setJson(jobj);
        } catch (ModelException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void jsonPost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
