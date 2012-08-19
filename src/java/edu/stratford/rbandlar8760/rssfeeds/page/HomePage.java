/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stratford.rbandlar8760.rssfeeds.page;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.click.Page;

public class HomePage extends Page {

    public String title = "HomeTtest";
    public String d;

    public HomePage() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        d = dateFormat.format(date).toString();
        addModel("title",title);
        addModel("d",d);
    }
}
