package com.football.college.web;

import java.util.Date;
import com.football.college.domain.User;

public class UserContainer
{
   protected User user;
   protected Date lastAccess;
   
   public User getUser()
   {
      return user;
   }
   public void setUser(User user)
   {
      this.user = user;
   }
   public Date getLastAccess()
   {
      return lastAccess;
   }
   public void setLastAccess(Date lastAccess)
   {
      this.lastAccess = lastAccess;
   }
   
   
}
