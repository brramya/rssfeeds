/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.page;

import org.apache.click.Context;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.click.Page;

/**
 * Generate a JSON Response for use with Dojo for a logged in
 * user only.
 *
 * @author Ramya
 * @see com.prime.pcd.page.SecureBasePage
 */
public abstract class JsonPage extends Page{
    private static final long serialVersionUID = 104L;

    /**
     *
     */
    protected JSONObject json;
    /**
     *
     */
    protected Map jsonMap;
    /**
     *
     */
    protected int errorStatus;
    protected String errorMessage;
    /**
     * Send JSON response to user agent
     */
    
    public void onGet() {
        jsonGet();
        final HttpServletResponse response = getContext().getResponse();

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Pragma", "no-cache");

        try {
            final PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
            json.clear(); // clear map after writing it
        } catch (java.io.IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Retrieve JSON response from user agent and process
     */
    
    public void onPost() {
        json = null;
        final Context c = getContext();
        final String jsonText = c.getRequestParameter("json");
        final JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {

            public List creatArrayContainer() {
                return new LinkedList();
            }

            public Map createObjectContainer() {
                return new LinkedHashMap();
            }
        };

        try {
            jsonMap = (Map) parser.parse(jsonText, containerFactory);
        } catch (ParseException pe) {
            System.out.println("JSON Parsing error: " + pe.getMessage());
        }
        jsonPost();

        // in this case we want to return a json response as well as the post processing.
        if (json != null) {
            final HttpServletResponse response = c.getResponse();
            response.setContentType("text/plain");
            response.setHeader("Pragma", "no-cache");
            if( errorStatus > 299 ){
                response.setStatus(errorStatus);
            }

            try {
                final PrintWriter writer = response.getWriter();
                writer.print(json);
                writer.flush();
                json.clear(); // clear map after writing it
            } catch (java.io.IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Handle the JSON get event.
     * This should be implemented in sub classes.
     */
    public abstract void jsonGet();

    /**
     * Handle a JSON post event.
     * This should be implemented in sub classes.
     */
    public abstract void jsonPost();

    /**
     * Get instance of JSON object.
     *
     * @return populated JSON object or null if undefined.
     */
    public JSONObject getJson() {
        return json;
    }

    /**
     * JSON object that should be output to the browser.
     *
     * @param jsonobj object ready to send.
     */
    public void setJson(final JSONObject jsonobj) {
        if (jsonobj != null) {
            json = jsonobj;
        } else {
            throw new IllegalArgumentException("JSON object cannot be null.");
        }
    }

    /**
     * JSONObject send as an error to the user, error status (417)
     * @param jsonobj object with error message to be sent to the user
     */

    public void sendJsonError(final JSONObject jsonobj){
        if (jsonobj != null) {
            json = jsonobj;
            errorStatus = HttpServletResponse.SC_EXPECTATION_FAILED;
        } else {
            throw new IllegalArgumentException("JSON object cannot be null.");
        }
    }
}
