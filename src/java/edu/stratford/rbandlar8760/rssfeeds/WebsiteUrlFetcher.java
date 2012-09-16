/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds;

import edu.stratford.rbandlar8760.rssfeeds.db.WebsiteUrl;
import edu.stratford.rbandlar8760.rssfeeds.model.WebsiteUrlModel;
import java.util.List;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;

/**
 *
 * @author RamyaNari
 */
public class WebsiteUrlFetcher {

    public static void main(final String[] args) {
        final WorkQueue work = new WorkQueue((float) 1);

        ObjectContext context;
        try {
            context = DataContext.getThreadObjectContext();
        } catch (Exception e) {
            context = DataContext.createDataContext();
            DataContext.bindThreadObjectContext(context);
        }
       // DataContext.bindThreadObjectContext(context);

        WebsiteUrlModel wum = new WebsiteUrlModel();
        try {
            List<WebsiteUrl> wul = wum.list();
            //while (true) {

            for (final WebsiteUrl feedItem : wul) {

                final WebsiteUrlItem wi = new WebsiteUrlItem(feedItem.getUrl());
                
                work.execute(wi);

            }
           //Thread.sleep(1000 * 60 * 60);

            //}
            work.shutdown();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
