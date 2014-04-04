package com.football.college.service.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.football.college.domain.DomainObject;
import com.football.college.service.DAOException;
import com.football.college.service.PMF;
import com.google.appengine.api.datastore.Key;

public class BaseDAO
{
   private static final Logger log = Logger.getLogger(BaseDAO.class.getName());

   protected void handleException(Logger log, String message, Exception e) throws DAOException
   {
      log.severe(log.getName() + ":" + message + ":" + e.toString());
      throw new DAOException(e);
   }

   /**
    * loadMany
    * 
    * @return
    * @throws DAOException
    */
   
   public <T extends DomainObject> List<T> loadMany(Class<T> clazz) throws DAOException
   {
      return this.loadMany(clazz, null);
   }
   
   /**
    * @param <T>
    * @param clazz
    * @param orderBy
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> List<T> loadMany(Class<T> clazz, String orderBy) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      Query q = pm.newQuery(clazz);

      if (orderBy != null)
         q.setOrdering(orderBy);

      List<T> results = null;

      try
      {
         results = (List<T>) q.execute();
         return (List<T>) pm.detachCopyAll(results);
      }
      catch (Exception e)
      {
         handleException(log, "loadMany Failed", e);
      }
      finally
      {
         q.closeAll();
         pm.close();
      }

      return null;
   }

   /**
    * loadMany
    * 
    * @param <T>
    * @param clazz
    * @param value
    * @param column
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> List<T> loadMany(Class<T> clazz, Object value, String column) throws DAOException
   {
      return this.loadMany(clazz, value, column, null);
   }
   
   /**
    * loadMany
    * 
    * @param <T>
    * @param clazz
    * @param value
    * @param column
    * @param orderBy
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> List<T> loadMany(Class<T> clazz, Object value, String column, String orderBy) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      Query q = pm.newQuery(clazz, column + " == searchvalue ");

      if (value instanceof String)
         q.declareParameters("String searchvalue");

      if (value instanceof Integer)
         q.declareParameters("Integer searchvalue");

      if (orderBy != null)
         q.setOrdering(orderBy);
      
      List<T> results, detached = null;

      try
      {
         results = (List<T>) q.execute(value);
         detached = (List<T>) pm.detachCopyAll(results);
         return detached;
      }
      catch (Exception e)
      {
         handleException(log, "load by value Failed", e);
      }
      finally
      {
         q.closeAll();
         pm.close();
      }

      return null;
   }
   
   /**
    * @param <T>
    * @param clazz
    * @param value
    * @param column
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> T load(Class<T> clazz, Object value, String column) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      Query q = pm.newQuery(clazz, column + " == searchvalue ");

      if (value instanceof String)
         q.declareParameters("String searchvalue");

      if (value instanceof Integer)
         q.declareParameters("Integer searchvalue");

      List<T> results, detached = null;

      try
      {
         results = (List<T>) q.execute(value);
         detached = (List<T>) pm.detachCopyAll(results);
         
         if (detached != null && !detached.isEmpty())
         {
            return detached.get(0);
         }
      }
      catch (Exception e)
      {
         handleException(log, "load by value Failed", e);
      }
      finally
      {
         q.closeAll();
         pm.close();
      }

      return null;
   }

   /**
    * @param <T>
    * @param clazz
    * @param value
    * @param column
    * @param valueTwo
    * @param columnTwo 
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> T load(Class<T> clazz, Object value, String column, Object valueTwo, String columnTwo) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      Query q = pm.newQuery(clazz);
      q.setFilter(column + " == searchvalue && " + columnTwo + " == searchvalueTwo");
      StringBuffer sb = new StringBuffer();
      
      if (value instanceof String)
         sb.append("String searchvalue");

      if (value instanceof Integer)
         sb.append("Integer searchvalue");

      sb.append(",");
      
      if (valueTwo instanceof String)
         sb.append("String searchvalueTwo");

      if (valueTwo instanceof Integer)
         sb.append("Integer searchvalueTwo");
      
      q.declareParameters(sb.toString());
      
      List<T> results, detached = null;

      try
      {
         results = (List<T>) q.execute(value, valueTwo);
         
         if(results != null)
            detached = (List<T>) pm.detachCopyAll(results);
         
         if (detached != null && !detached.isEmpty())
         {
            return detached.get(0);
         }
      }
      catch (Exception e)
      {
         handleException(log, "load by value Failed", e);
      }
      finally
      {
         q.closeAll();
         pm.close();
      }

      return null;
   }
   
   /**
    * @param <T>
    * @param domainObject
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> T save(T domainObject) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();

      try
      {
         return (T) pm.detachCopy(  pm.makePersistent(domainObject) );
      }
      catch (Exception e)
      {
         handleException(log, "save Failed", e);
      }
      finally
      {
         pm.close();
      }
      return null;
   }

   /**
    * load
    * 
    * @param <T>
    * @param clazz
    * @param id
    * @return
    * @throws DAOException
    */
   public <T extends DomainObject> T load(Class<T> clazz, Long id) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      try
      {
         return pm.detachCopy( pm.getObjectById(clazz, id) );
      }
      catch (Exception e)
      {
         handleException(log, "load Failed", e);
      }
      finally
      {
         pm.close();
      }

      return null;
   }

   /**
    * delete
    * 
    * @param <T>
    * @param domainObject
    * @throws DAOException
    */
   public <T extends DomainObject> boolean delete(T domainObject) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      try
      {
         pm.deletePersistent(domainObject);
         return false;
      }
      catch (Exception e)
      {
         handleException(log, "delete Failed", e);
      }
      finally
      {
         pm.close();
      }
      
      return true;
   }
   
   /**
    * delete
    * 
    * @param <T>
    * @param domainObject
    * @throws DAOException
    */
   public <T extends DomainObject> boolean delete(Class<T> clazz, Key key) throws DAOException
   {
      PersistenceManager pm = PMF.get().getPersistenceManager();
      try
      {
         pm.deletePersistent(pm.getObjectById(clazz, key));
         return false;
      }
      catch (Exception e)
      {
         handleException(log, "delete Failed", e);
      }
      finally
      {
         pm.close();
      }
      
      return true;
   }
   
}
