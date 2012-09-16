/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;
import edu.stratford.rbandlar8760.rssfeeds.db.ProductDetails;
import edu.stratford.rbandlar8760.rssfeeds.model.ProductDetailsModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author RamyaNari
 */
public class GenerateRssFeed extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        doPost(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        try {
            String searchString = request.getParameter("s");
            String num = request.getParameter("num");
            String[] ss = null;

            if( searchString != null )
             ss = searchString.split("/");
            else
                ss = new String[0];
            List<String> ssl = Arrays.asList(ss);
            ProductDetailsModel pdm = new ProductDetailsModel();
            List<ProductDetails> pdl = pdm.searchTitle(ssl);

            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");
            feed.setTitle("Feed for search");
            feed.setDescription("Feeds has data for search string" + searchString);
            feed.setLink("http://localhost:8084/RSS_FEEDS/generateRssFeed");

            List entries = new ArrayList();

            for (ProductDetails pd : pdl) {
                
                SyndEntry entry = new SyndEntryImpl();
                SyndContent description = new SyndContentImpl();;
                entry.setTitle( pd.getTitle());
                entry.setLink(pd.getLink());
                entry.setPublishedDate(pd.getDate());
                description.setType("text/html");
                description.setValue(pd.getDescription());
                entry.setDescription(description);
                entries.add(entry);
            }

            feed.setEntries(entries);

            response.setContentType("text/html; charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            SyndFeedOutput output = new SyndFeedOutput();
            PrintWriter pr = response.getWriter();
            output.output(feed, pr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
