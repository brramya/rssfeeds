/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import edu.stratford.rbandlar8760.rssfeeds.db.MappingDetails;
import edu.stratford.rbandlar8760.rssfeeds.db.ProductDetails;
import edu.stratford.rbandlar8760.rssfeeds.db.WebsiteUrl;
import edu.stratford.rbandlar8760.rssfeeds.model.CategoryModel;
import edu.stratford.rbandlar8760.rssfeeds.model.InformationModel;
import edu.stratford.rbandlar8760.rssfeeds.model.MappingDetailsModel;
import edu.stratford.rbandlar8760.rssfeeds.model.ProductDetailsModel;
import edu.stratford.rbandlar8760.rssfeeds.model.WebsiteModel;
import edu.stratford.rbandlar8760.rssfeeds.model.WebsiteUrlModel;
import java.net.URL;
import java.util.Date;
import java.util.List;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;

/**
 *
 * @author RamyaNari
 */
public class WebsiteUrlItem implements Runnable {

    private String url;
    static final FeedFetcherCache websiteUrlCache = HashMapFeedInfoCache.getInstance();

    public WebsiteUrlItem(final String url) {
        this.url = url;
    }

    public void run() {
        ObjectContext context;
        try {
            context = DataContext.getThreadObjectContext();
        } catch (Exception e) {
            context = DataContext.createDataContext();
            DataContext.bindThreadObjectContext(context);
        }

        try {
            WebsiteUrlModel wum = new WebsiteUrlModel();
            final URL feedUrl = new URL(url);

            final FeedFetcher fetcher = new HttpURLFeedFetcher(websiteUrlCache);
            final WebsiteUrlEventListenerImpl listener = new WebsiteUrlEventListenerImpl();
            fetcher.addFetcherEventListener(listener);

            final SyndFeed syncfeed = fetcher.retrieveFeed(feedUrl);



            InformationModel im = new InformationModel();
            WebsiteModel wm = new WebsiteModel();
            WebsiteUrl wu = wum.lookupUrl(url);
            MappingDetailsModel mdm = new MappingDetailsModel();

            // System.out.println("Current category:" + curCat.getCategoryName());
            //System.out.println("\n\n\nURL:"+feed.getLink());




            final List<SyndEntryImpl> feeds = syncfeed.getEntries();

            //System.out.println("Cat length:"+categories.size());
            for (final SyndEntryImpl feed : feeds) {
                String title = feed.getTitle();
                Category curCat = null;
                CategoryModel catM = new CategoryModel();

                List<Category> cl = catM.list();

                for (final Category c : cl) {
                    if (title.toLowerCase().indexOf(c.getCategoryName().toLowerCase()) != -1) {

                        curCat = c;
                        break;
                    }
                }

                if (curCat != null) {
                    MappingDetails md = mdm.lookUpAndCreateMappingDetails(wu.getToWebsite(), curCat);
                    ProductDetailsModel pdm = new ProductDetailsModel();

                    ProductDetails pd = pdm.lookup(wu.getToWebsite(), title);

                    if (pd == null) {
                        Date pubDate = feed.getPublishedDate();

                        String link = feed.getLink();
                        String description = feed.getDescription().getValue();

                        pdm.create(md, title, pubDate, description, link);

                    }
                }

                //System.out.println("\n\n\nTime:" + cat.getPublishedDate() + "\n\n\n");
                //String t = cat.getTaxonomyUri();
                //  System.out.println("\n\n\nC Title:"+cat.getTitle());
                // System.out.println("\n\n:Author"+cat.getAuthor());
                //System.out.println("\n\n:Link"+cat.getLink());
                // System.out.println("\n\nURI"+cat.getUri());
                // System.out.println("\n\nDate"+cat.getPublishedDate());
                // SyndFeed sf = cat.getSource();
                // System.out.println("\n\n\nSyncfeed descr"+sf.getDescription());

            }

            //System.out.println("\n\n\n\nDescription:" + feed.getDescription());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    static class WebsiteUrlEventListenerImpl implements FetcherListener {

        /**
         * @see com.sun.syndication.fetcher.FetcherListener#fetcherEvent(com.sun.syndication.fetcher.FetcherEvent)
         */
        public void fetcherEvent(final FetcherEvent event) {
            final String eventType = event.getEventType();
            if (FetcherEvent.EVENT_TYPE_FEED_POLLED.equals(eventType)) {
                System.out.println("\tEVENT: Feed Polled. URL = " + event.getUrlString());
            } else if (FetcherEvent.EVENT_TYPE_FEED_RETRIEVED.equals(eventType)) {
                System.out.println("\tEVENT: Feed Retrieved. URL = " + event.getUrlString());
            } else if (FetcherEvent.EVENT_TYPE_FEED_UNCHANGED.equals(eventType)) {
                System.out.println("\tEVENT: Feed Unchanged. URL = " + event.getUrlString());
            }
        }
    }
}
