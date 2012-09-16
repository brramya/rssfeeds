/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Information;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author RamyaNari
 */
public class InformationModel extends BaseModel {
public List<Information> list() throws ModelException
    {
        try
        {
            final SelectQuery query = new SelectQuery( Information.class );
            @SuppressWarnings("unchecked")
            final List<Information> inf = dataContext.performQuery( query );
            if (inf.isEmpty())
            {
                throw new ModelException( "Could not lookup information" );
            }
            return inf;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( "Database error trying to list information list" );
        }
    }


  public Information lookUpInformation( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid information name");

      try
        {
            Expression qualifier = Expression.fromString( "information = $name" );
            final HashMap<String, Object> map = new HashMap<String, Object>( 2 );
            map.put( "name", name );
            qualifier = qualifier.expWithParameters( map );

            final SelectQuery query = new SelectQuery( Information.class, qualifier );
            @SuppressWarnings("unchecked")
            final List<Information> category = dataContext.performQuery( query );
            return category.get( 0 );
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( ce.getMessage() );
        }

  }

  public Information create( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Informaation name");

      try{
          final Information ca = dataContext.newObject(Information.class);
          ca.setInformation(name);
          dataContext.commitChanges();
          return ca;
      }catch (CayenneRuntimeException ce)
      {
          dataContext.rollbackChanges();
          throw new ModelException("Could not add Information");
      }
  }
}
