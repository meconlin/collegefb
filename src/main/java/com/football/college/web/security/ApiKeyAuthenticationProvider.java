package com.football.college.web.security;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.football.college.service.FootballUserService;
import com.football.college.service.ServiceException;


/**
 * @author mark
 *
 */
public class ApiKeyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
{
   @Autowired
   private FootballUserService footballUserService;
   private static final Logger log = Logger.getLogger(ApiKeyAuthenticationProvider.class.getName());
   
   @Override
   protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException
   {
      // TODO Auto-generated method stub

   }

   @Override
   protected UserDetails retrieveUser(String apiKey, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException
   {
      UserDetails loadedUser = null;

      // find user by api key from short lived data store
      // key -> use value store
      //
      // key found and not expired setup user details
      //
      // else throw exception
      
      log.info("api key authenticate : apikey : " + apiKey);
      
      com.football.college.domain.User u = null;
      try
      {
         u = footballUserService.findUserByActiveApiKey(apiKey);
      }
      catch (ServiceException e)
      {
         log.severe("no user found for api key : " + apiKey);
      }
      
      if(u != null && u.getUsername() != null &&!u.getUsername().isEmpty())  //for now pretend any key is valid
      { 
         loadedUser = footballUserService.loadUserByUsername(u.getUsername());
      } else {
         throw new AuthenticationServiceException("No valid key found");  
      }
      
      return loadedUser;
   }

   /*************************************************/
   

}