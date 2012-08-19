/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.access.DataContext;

/**
 *
 * @author RamyaNari
 */
public class BaseModel {

     /** Cayenne Data context */
    protected DataContext dataContext;
     public BaseModel()
    {
        try
        {
            dataContext = (DataContext) BaseContext.getThreadObjectContext();
        }
        catch (Exception e)
        {

            dataContext = DataContext.createDataContext(); // for junit testing
        }
    }

}
