package com.football.college.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class User extends DomainObject
{
   public static String ADMIN = "admin";
   
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   protected Long id;
   
   @Persistent
   protected String username;

   @Persistent
   protected String password;

   @Persistent
   protected String role;
   
   public enum Role {
      ROLE_ADMIN, ROLE_USER
   }
      
   public User()
   {
      
   }
   
   public User(String username, String password, User.Role role)
   {
      this.username = username;
      this.password = password;
      this.role = role.name();
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

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public String getRole()
   {
      return role;
   }

   public void setRole(String role)
   {
      this.role = role;
   }

}
