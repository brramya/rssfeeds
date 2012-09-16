/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds.page;

import edu.stratford.rbandlar8760.rssfeeds.db.Information;
import edu.stratford.rbandlar8760.rssfeeds.db.Website;
import edu.stratford.rbandlar8760.rssfeeds.db.WebsiteUrl;
import edu.stratford.rbandlar8760.rssfeeds.model.InformationModel;
import edu.stratford.rbandlar8760.rssfeeds.model.ModelException;
import edu.stratford.rbandlar8760.rssfeeds.model.TagDetailsModel;
import edu.stratford.rbandlar8760.rssfeeds.model.WebsiteModel;
import edu.stratford.rbandlar8760.rssfeeds.model.WebsiteUrlModel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RamyaNari
 */
public class JsonInformationPage extends JsonPage {

    @Override
    public void jsonGet() {
        final JSONObject jobj = new JSONObject();
        final JSONArray items = new JSONArray();


        try {
            InformationModel im = new InformationModel();
            final List<Information> infl = im.list();

            int i = 0;
            for (final Information inf : infl) {
                final JSONObject contents = new JSONObject();

                contents.put("inf_name", inf.getInformation());
                contents.put("id", i++);

                items.add(contents);
            }
            infl.clear();

            jobj.put("items", items);
            jobj.put("label", "inf_name");
            jobj.put("identifier", "id");

            setJson(jobj);
        } catch (ModelException me) {
            System.err.println(me.getMessage());

        }
    }

    @Override
    public void jsonPost() {

        String url = null;
        String website = null;
        String title = null;
        String guid = null;
        String link = null;
        String pubDate = null;
        String description = null;
        String type = null;
        if (jsonMap != null) {
            for (Iterator iter = jsonMap.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry entry = (Map.Entry) iter.next();
                System.out.println(entry.getKey() + "=>" + entry.getValue());

                if (entry.getKey().equals("url")) {
                    url = (String) entry.getValue();
                } else if (entry.getKey().equals("website")) {
                    website = (String) entry.getValue();
                } else if (entry.getKey().equals("title")) {
                    title = (String) entry.getValue();
                } else if (entry.getKey().equals("guid")) {
                    guid = (String) entry.getValue();
                } else if (entry.getKey().equals("link")) {
                    link = (String) entry.getValue();
                } else if (entry.getKey().equals("pubDate")) {
                    pubDate = (String) entry.getValue();
                } else if (entry.getKey().equals("description")) {
                    description = (String) entry.getValue();
                } else if (entry.getKey().equals("type")) {
                    type = (String) entry.getValue();
                }

            }


        }

        if ("addRssfeedUrl".equals(type)) {

            WebsiteModel wm = new WebsiteModel();
            WebsiteUrlModel wum = new WebsiteUrlModel();
            InformationModel im = new InformationModel();
            try {
                //Website web = wm.checkAndCreate(website);
                Website web = wm.lookUpWebsite(website);

                if (web == null) {
                    web = wm.create(website);
                    TagDetailsModel tdm = new TagDetailsModel();

                    List<Information> iml = im.list();

                    for (Information i : iml) {
                        if ("title".equals(i.getInformation())) {
                            tdm.create(web, i, title);
                        } else if ("guid".equals(i.getInformation())) {
                            tdm.create(web, i, guid);
                        } else if ("link".equals(i.getInformation())) {
                            tdm.create(web, i, link);
                        } else if ("pubDate".equals(i.getInformation())) {
                            tdm.create(web, i, pubDate);
                        } else if ("description".equals(i.getInformation())) {
                            tdm.create(web, i, description);
                        }
                    }

                }
                
                WebsiteUrl wu = wum.checkAndCreateWebsiteUrl(web, url);

            } catch (ModelException me) {
                me.printStackTrace();
            }

        }
    }
}
