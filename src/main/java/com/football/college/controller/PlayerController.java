package com.football.college.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.football.college.domain.PickWeek;
import com.football.college.domain.Week;
import com.football.college.domain.api.ApiLeaderboard;
import com.football.college.domain.api.ApiPickWeek;
import com.football.college.domain.api.ApiScoreboard;
import com.football.college.domain.api.ApiWeek;
import com.football.college.service.ServiceException;
import com.football.college.service.WeekService;

@Controller
@RequestMapping("/player")
public class PlayerController extends BaseController
{
   private static final Logger log = Logger.getLogger(PlayerController.class.getName());
   
   @Autowired
   private WeekService         weekService;
   
   /**
    * pickweek save
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/pickweek/picks/save", method = RequestMethod.POST)
   public @ResponseBody
   boolean pickWeekSave(@RequestBody ApiPickWeek week)
   {
      log.fine("ApiPickWeek from post : " + week);
      
      // pull username from usercontainer and place on object, 
      // in case ui doesnt care or doesnt send back up
      //
      String username = this.getUsername();
      week.setUsername(username);

      try
      {
         weekService.submitApiWeekPicks(week);
         return true;
      }
      catch (ServiceException e)
      {
         log.severe("pickWeekSave : Exception : " + e.getMessage());
      }

      return false;
   }

   /**
    * weeks
    * List of all weeks - not specific to this player
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/weeks", method = RequestMethod.GET)
   public @ResponseBody
   List<ApiWeek> weeks()
   {
      List<Week> weeks = null;
      List<ApiWeek> apiweeks = null;

      try
      {
         // get all weeks, even if not complete
         //
         weeks = weekService.findAllWeeks(false);

         if (weeks != null)
         {
            apiweeks = new ArrayList<ApiWeek>();
            for (Week w : weeks)
            {
               if (w.isPublished())                 //dont show if not published yet
               {
                  apiweeks.add(new ApiWeek(w));
               }
            }
         }

      }
      catch (ServiceException e)
      {
         log.severe("Failure to get weeks : ");
      }

      return apiweeks;
   }

   /**
    * leaderboard
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/leaderboard", method = RequestMethod.GET)
   public @ResponseBody
   ApiLeaderboard leaderboard()
   {
      ApiLeaderboard answer = new ApiLeaderboard();
      
      try
      {
         answer = weekService.constructLeaderboard();
         return answer;
      }
      catch (ServiceException e)
      {
         log.severe("Failure to get leaderboard : ");
      }

      return answer;
   }
   
   /**
    * scoreboard by number
    * get a scoreboard by number
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/scoreboard/{weeknumber}", method = RequestMethod.GET)
   public @ResponseBody
   ApiScoreboard scoreboard(@PathVariable String weeknumber)
   {
      try
      {
         ApiScoreboard answer = weekService.constructScoreboard(weeknumber);
         return answer;
      }
      catch (ServiceException e)
      {
         log.severe("Failure to get scoreboard : weeknumber : " + weeknumber);
      }

      return null;
   }
   
   /**
    * pickweek by number
    * get a pickweek by number
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/pickweek/{weeknumber}", method = RequestMethod.GET)
   public @ResponseBody
   ApiPickWeek pickweek(@PathVariable String weeknumber)
   {
      PickWeek pw = null;
      ApiPickWeek apw = null;

      try
      {
         // service will handle new/null case and updates from week by admin
         //
         pw = weekService.findPickWeek(weeknumber, this.getUsername());

         // convert to API
         //
         apw = new ApiPickWeek(pw);
         apw.setUsername(null); //client doesnt really need username ?
      }
      catch (ServiceException e)
      {
         log.severe("Failure to get pick week : weeknumber : username : " + weeknumber);
      }

      return apw;
   }

}
