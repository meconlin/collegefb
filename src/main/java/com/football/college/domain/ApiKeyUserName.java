package com.football.college.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author mark
 *
 */
@PersistenceCapable(detachable = "true")
public class ApiKeyUserName extends DomainObject
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   protected Long id;
   
   @Persistent
   protected String apikey;

   @Persistent
   protected String username;

   @Persistent
   protected Date lastaccess;
   

   public ApiKeyUserName()
   {
      
   }
   
   public ApiKeyUserName(String username, String apikey)
   {
      this.username = username;
      this.apikey = apikey;
      this.lastaccess = new Date();
   }

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public String getApikey()
   {
      return apikey;
   }

   public void setApikey(String apikey)
   {
      this.apikey = apikey;
   }

   public Date getLastaccess()
   {
      return lastaccess;
   }

   public void setLastaccess(Date lastaccess)
   {
      this.lastaccess = lastaccess;
   }

  /***** Helpers *******/
   
   public boolean stillActive()
   {
     return true;
   }
   
   
}
