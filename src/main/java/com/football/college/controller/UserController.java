package com.football.college.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.football.college.domain.User;
import com.football.college.service.FootballUserService;
import com.football.college.service.ServiceException;

@Controller
@RequestMapping("/uc")
public class UserController
{
   private static final Logger log = Logger.getLogger(UserController.class.getName());
  
   @Autowired
   private FootballUserService       footballUserService;

   /**
    * userDelete
    * 
    * @param weeknumber
    * @return
    */
   @RequestMapping(value = "/user/delete/{usernumber}", method = RequestMethod.GET)
   public @ResponseBody
   boolean userDelete(@PathVariable String usernumber)
   {
      try
      {
         return footballUserService.delete(Long.parseLong( usernumber ) );
      }
      catch (ServiceException e)
      {
         log.error("userDelete : Exception : " + e.getMessage());
      }

      return false;
   }
   
   /**
    * userSave
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/user/save", method = RequestMethod.POST)
   public @ResponseBody
   boolean userSave(@RequestBody User user)
   {
      log.info("User from post : " + user);
      
      try
      {
         footballUserService.save(user);
      }
      catch (ServiceException e)
      {
         log.error("userSave : Exception : " + e.getMessage());
      }

      return true;
   }
   
   @RequestMapping(value = "/users", method = RequestMethod.GET)
   @ResponseBody
   public List<User> users(HttpServletRequest request)
   {      
      try
      { 
         return footballUserService.loadUsers();
      }
      catch (ServiceException e)
      {
         log.error("Failure to load users");
      }

      return null;
   }

   @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
   public @ResponseBody
   User user(@PathVariable Long userid)
   {
      try
      {
         return footballUserService.load(userid);
      }
      catch (ServiceException e)
      {
         log.error("Failure to load user : " + userid);
      }

      return null;
   }

   @RequestMapping(value = "/user/find/{username}", method = RequestMethod.GET)
   public @ResponseBody
   User user(@PathVariable String username)
   {
      try
      {
         return footballUserService.load(username);
      }
      catch (ServiceException e)
      {
         log.error("Failure to load user : " + username);
      }

      return null;
   }
   
}
