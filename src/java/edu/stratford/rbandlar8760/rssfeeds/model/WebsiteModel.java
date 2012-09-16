/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Website;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author RamyaNari
 */
public class WebsiteModel extends BaseModel{
public List<Website> list() throws ModelException
    {
        try
        {
            final SelectQuery query = new SelectQuery( Website.class );
            @SuppressWarnings("unchecked")
            final List<Website> ws = dataContext.performQuery( query );
            if (ws.isEmpty())
            {
                throw new ModelException( "Could not lookup website" );
            }
            return ws;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( "Database error trying to list website list" );
        }
    }


  public Website lookUpWebsite( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Website name");

      try
        {
            Expression qualifier = Expression.fromString( "websiteName = $name" );
            final HashMap<String, Object> map = new HashMap<String, Object>( 2 );
            map.put( "name", name );
            qualifier = qualifier.expWithParameters( map );

            final SelectQuery query = new SelectQuery( Website.class, qualifier );
            @SuppressWarnings("unchecked")
            final List<Website> webSite = dataContext.performQuery( query );
            if(!webSite.isEmpty())
                return webSite.get( 0 );
            return null;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( ce.getMessage() );
        }

  }

  public Website create( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Website name");

      try{
          final Website ca = dataContext.newObject(Website.class);
          ca.setWebsiteName(name);
          
          dataContext.commitChanges();
          return ca;
      }catch (Exception ce)
      {
          ce.printStackTrace();
          dataContext.rollbackChanges();
          throw new ModelException("Could not add Website");
      }
  }

  public Website checkAndCreate( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Website name");
          try{
            Website w = lookUpWebsite(name);
            System.out.println("Website:"+w+":");
            if( w == null ){
                Website web = create( name );
                return web;
            }
            return w;
          }catch(ModelException me ){
            throw new ModelException(me.getMessage());
          }
  }
}
