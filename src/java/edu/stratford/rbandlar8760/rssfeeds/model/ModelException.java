/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

/**
 *
 * @author RamyaNari
 */


    public class ModelException extends java.lang.Exception {

    private static final long serialVersionUID = 120L;

    /**
     *
     */
    public ModelException()
    {
        super( "Invalid model operation" );
    }

    /**
     *
     * @param message model exception message
     */
    public ModelException( final String message )
    {
        super( message );
    }
}


