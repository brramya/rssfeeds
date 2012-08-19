/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author RamyaNari
 */
public class CategoryModel extends BaseModel{

  public List<Category> list() throws ModelException
    {
        try
        {
            final SelectQuery query = new SelectQuery( Category.class );
            @SuppressWarnings("unchecked")
            final List<Category> art = dataContext.performQuery( query );
            if (art.isEmpty())
            {
                throw new ModelException( "Could not lookup category" );
            }
            return art;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( "Database error trying to list category list" );
        }
    }


  public Category lookUpCategory( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Category name");

      try
        {
            Expression qualifier = Expression.fromString( "categoryName = $name" );
            final HashMap<String, Object> map = new HashMap<String, Object>( 2 );
            map.put( "name", name );
            qualifier = qualifier.expWithParameters( map );

            final SelectQuery query = new SelectQuery( Category.class, qualifier );
            @SuppressWarnings("unchecked")
            final List<Category> category = dataContext.performQuery( query );
            return category.get( 0 );
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( ce.getMessage() );
        }

  }

  public Category create( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Category name");

      try{
          final Category ca = dataContext.newObject(Category.class);
          ca.setCategoryName(name);
          dataContext.commitChanges();
          return ca;
      }catch (CayenneRuntimeException ce)
      {
          dataContext.rollbackChanges();
          throw new ModelException("Could not add Category");
      }
  }
}
