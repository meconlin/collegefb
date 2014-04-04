package com.football.college.web.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.football.college.web.UserContainer;

public class ApiKeyFilter extends OncePerRequestFilter
{
   private static final Logger log = Logger.getLogger(ApiKeyFilter.class.getName());

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
   {
      log.info("ApiKeyFilter : START ");
      String apikey = request.getParameter("apikey");

      log.info("ApiKeyFilter : apikey : " + apikey);
      
      if(true)
      //if (apikey != null)
      {
         ServletContext servletContext = request.getSession().getServletContext();
         UserContainer uc = (UserContainer) servletContext.getAttribute(apikey);

         //if (uc != null)
         if(true)
         {
            
            request.getSession().setAttribute("usercontainer", uc);
            
            
            log.info("ApiKey Filter was a success");
            filterChain.doFilter(request, response);
         }
         
      }

      log.info("ApiKeyFilter : END : NO USER FOUND FOR KEY ! : " + apikey);
   }

   /**
    * hasExpired
    * 
    * @param u
    * @return
    */
   private boolean hasExpired(UserContainer u)
   {
      return true;
   }

   
}
