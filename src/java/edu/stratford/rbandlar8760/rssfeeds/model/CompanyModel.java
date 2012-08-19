/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds.model;

import edu.stratford.rbandlar8760.rssfeeds.db.Category;
import edu.stratford.rbandlar8760.rssfeeds.db.Company;
import java.util.HashMap;
import java.util.List;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author RamyaNari
 */
public class CompanyModel extends BaseModel{
    public List<Company> list() throws ModelException
    {
        try
        {
            final SelectQuery query = new SelectQuery( Company.class );
            @SuppressWarnings("unchecked")
            final List<Company> com = dataContext.performQuery( query );
            if (com.isEmpty())
            {
                throw new ModelException( "Could not lookup company" );
            }
            return com;
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( "Database error trying to list company list" );
        }
    }


  public Company lookUpCompany( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Company name");

      try
        {
            Expression qualifier = Expression.fromString( "companyName = $name" );
            final HashMap<String, Object> map = new HashMap<String, Object>( 2 );
            map.put( "name", name );
            qualifier = qualifier.expWithParameters( map );

            final SelectQuery query = new SelectQuery( Company.class, qualifier );
            @SuppressWarnings("unchecked")
            final List<Company> company = dataContext.performQuery( query );
            return company.get( 0 );
        }
        catch (CayenneRuntimeException ce)
        {
            throw new ModelException( ce.getMessage() );
        }

  }

  public Company create( String name ) throws ModelException{

      if ( name == null || name.length() == 0 )
          throw new IllegalArgumentException("Invalid Company name");

      try{
          final Company ca = dataContext.newObject(Company.class);
          ca.setCompanyName(name);
          dataContext.commitChanges();
          return ca;
      }catch (CayenneRuntimeException ce)
      {
          dataContext.rollbackChanges();
          throw new ModelException(ce.getMessage());
      }
  }
}
