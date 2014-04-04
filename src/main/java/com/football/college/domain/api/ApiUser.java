package com.football.college.domain.api;

import com.football.college.domain.User;

public class ApiUser
{
   public String username;
   public String role;
   public String apikey;
   
   public ApiUser()
   {
      
   }
   
   public ApiUser(User u, String apiKey)
   {
      this.username = u.getUsername();
      this.role = u.getRole();
      this.apikey = apiKey;
   }
   
   public String getUsername()
   {
      return username;
   }
   public void setUsername(String username)
   {
      this.username = username;
   }
   public String getRole()
   {
      return role;
   }
   public void setRole(String role)
   {
      this.role = role;
   }
   public String getApikey()
   {
      return apikey;
   }
   public void setApikey(String apikey)
   {
      this.apikey = apikey;
   } 
}
