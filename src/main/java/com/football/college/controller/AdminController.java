package com.football.college.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.football.college.domain.User;
import com.football.college.domain.Week;
import com.football.college.domain.api.ApiWeek;
import com.football.college.service.FootballUserService;
import com.football.college.service.ServiceException;
import com.football.college.service.WeekService;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController
{
   private static final Logger log = Logger.getLogger(AdminController.class.getName());

   @Autowired
   private WeekService         weekService;

   @Autowired
   private FootballUserService footballUserService;

   /***********************************************************************************/

   /**
    * Proccess Scores
    * 
    */
   @RequestMapping(value = "/run/scores", method = RequestMethod.GET)
   public @ResponseBody
   boolean runscores()
   {
      log.info("runscores started: ");
      if (this.isAdmin())
      {
         try
         {
            weekService.scorePickWeeks();
            return true;
         }
         catch (ServiceException e)
         {
            log.error("runscores : Exception : " + e.getMessage());
         }
      }

      return false;
   }

   /**
    * weekDelete
    * 
    * @param weeknumber
    * @return
    */
   @RequestMapping(value = "/week/delete/{weeknumber}", method = RequestMethod.GET)
   public @ResponseBody
   boolean weekDelete(@PathVariable String weeknumber)
   {
      log.debug("weekDelete : called with weeknumber : " + weeknumber);
      if (this.isAdmin())
      {
         try
         {
            return weekService.deleteWeek(weeknumber);
         }
         catch (ServiceException e)
         {
            log.error("weekSave : Exception : " + e.getMessage());
         }

      }
      return false;
   }

   /**
    * weeks
    * List of all weeks
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

      if (this.isAdmin())
      {
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
                  apiweeks.add(new ApiWeek(w));
               }
            }

         }
         catch (ServiceException e)
         {
            log.error("Failure to get weeks : ");
         }
      }

      return apiweeks;
   }

   /**
    * weekSaveScores
    * ApiWeek expected from client to be saved, in this case we should getting new scores for the games
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/week/score/save", method = RequestMethod.POST)
   public @ResponseBody
   boolean weekSaveScores(@RequestBody ApiWeek week)
   {

      log.debug("ApiWeek from post : " + week);
      if (this.isAdmin())
      {
         try
         {
            Week resultweek = weekService.submitApiWeekScores(week);
            weekService.scorePickWeeks();
         }
         catch (ServiceException e)
         {
            log.error("weekSave : Exception : " + e.getMessage());
         }
      }
      return true;
   }

   /**
    * saveWeek
    * ApiWeek expected from client to be saved, this is suppose to be the save during week construction, adding and setting up games
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/week/save", method = RequestMethod.POST)
   public @ResponseBody
   boolean weekSave(@RequestBody ApiWeek week)
   {
      if (this.isAdmin())
      {
         log.debug("ApiWeek from post : " + week);

         try
         {
            Week resultweek = weekService.submitApiWeek(week);
         }
         catch (ServiceException e)
         {
            log.error("weekSave : Exception : " + e.getMessage());
         }
      }
      return true;
   }

   /**
    * week
    * ApiWeek pulled by number
    * 
    * @param week
    * @return
    */
   @RequestMapping(value = "/week/{weeknumber}", method = RequestMethod.GET)
   public @ResponseBody
   ApiWeek week(@PathVariable String weeknumber)
   {
      Week resultweek = null;
      ApiWeek apiweek = null;
      if (this.isAdmin())
      {
         try
         {
            resultweek = weekService.findWeekByNumber(weeknumber);

            // Week Not found or is an Add instead of Edit with no value (undefined)
            //
            if (resultweek == null)
            {
               apiweek = new ApiWeek(new Week());
            }
            else
            {
               apiweek = new ApiWeek(resultweek);
            }

         }
         catch (ServiceException e)
         {
            log.error("Failure to get week : " + weeknumber);
         }
      }
      return apiweek;
   }

   public boolean isAdmin()
   {
      String requestuser = this.getUsername();
      User u = null;
      try
      {
         u = this.footballUserService.load(requestuser);
      }
      catch (ServiceException e)
      {
         log.error("Failure to get user for admin check : " + requestuser);
      }

      if (u == null)
      {
         return false;
      }
      else
      {
         return User.Role.ROLE_ADMIN.name().equals(u.getRole());
      }

   }

   /*******************************************************************************/

}
