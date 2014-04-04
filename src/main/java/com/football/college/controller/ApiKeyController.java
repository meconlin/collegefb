package com.football.college.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.football.college.domain.ApiKeyUserName;
import com.football.college.domain.User;
import com.football.college.domain.Week;
import com.football.college.domain.api.ApiUser;
import com.football.college.service.FootballUserService;
import com.football.college.service.ServiceException;
import com.football.college.service.WeekService;
import com.football.college.web.security.ApiKeyUtil;

@Controller
@RequestMapping("/apikey")
public class ApiKeyController
{
   private static final Logger log = Logger.getLogger(ApiKeyController.class.getName());

   @Autowired
   private FootballUserService footballUserService;
   @Autowired
   private WeekService         weekService;

   @RequestMapping(value = "/warmer", method= RequestMethod.GET)
   public @ResponseBody boolean cachewarmer()
   {
      log.info("Warmers called");

      try
      {
         List<String> users = footballUserService.findAllPlayers();

         weekService.constructLeaderboard();
         List<Week> weeks = weekService.findAllWeeks(false);

         if(weeks != null && weeks.size()>0)
         {
            Week biggestweek = weeks.get(0);
         
            for (String username : users)
            {
               for (Week w : weeks)
               {
                  if( Integer.parseInt( w.getNumber() ) > Integer.parseInt( biggestweek.getNumber() ) )
                     biggestweek = w;
                  
                  weekService.findPickWeek(w.getNumber(), username);
                  
               }
            }
            
            // refresh newest scoreboard
            weekService.constructScoreboard(biggestweek.getNumber());
            weekService.constructLeaderboard();
         }
      }
      catch (Exception e)
      {
         log.severe("Warmers called and had and error : " + e.toString());
      }

      return true;
   }

   @RequestMapping(value = "/login", method = RequestMethod.POST)
   public @ResponseBody
   ApiUser login(@RequestBody User u) throws Exception
   {
      log.info("login : " + u.getUsername() + ":" + u.getPassword());
      try
      {
         // does the user even exist ?
         //
         u = footballUserService.lookup(u.getUsername(), u.getPassword());
         if (u != null)
         {
            //does this use have an active apikey already?
            //
            ApiKeyUserName existingapikeyusername = footballUserService.findUserWithActiveApiKeyByUsername(u.getUsername());
            String apikey = null;
            ApiUser apiuser = null;

            // if not create a new one
            if (existingapikeyusername == null)
            {
               //create key
               apikey = ApiKeyUtil.createApiKey();
               apiuser = new ApiUser(u, apikey);

               // persist relationship between this use and this apikey
               ApiKeyUserName apikeyusername = new ApiKeyUserName();
               apikeyusername.setApikey(apikey);
               apikeyusername.setUsername(u.getUsername());
               footballUserService.save(apikeyusername);
            }
            else
            {
               apikey = existingapikeyusername.getApikey();
               apiuser = new ApiUser(u, apikey);
            }

            return apiuser;
         }
      }
      catch (ServiceException e)
      {
         log.severe("Failed to login with credentials : " + u.getUsername());
      }

      //throw hard failure here so that login know things went bad
      throw new Exception("Failure to login");
   }

}
