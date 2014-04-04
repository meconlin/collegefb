package com.football.college.domain.api;

import com.football.college.domain.PickGame;
import com.football.college.domain.PickGameList;
import com.football.college.domain.PickWeek;

/**
 * ApiPickWeek
 * 
 * 
 * @author mark
 */
public class ApiPickWeek
{
   protected String        weeknumber;
   protected String        username;
   protected PickGameList  games;
   protected boolean       complete;
   
   /*******
    * TODO - write up this gotcha! BLANK CONSTRUCTOR REQUIRED FOR 
    * 
    * 
    */
   public ApiPickWeek()
   {
      
   }
   
   public ApiPickWeek(PickWeek pw)
   {
      this.weeknumber = pw.getNumber();
      this.username = pw.getUsername();
      this.games = new PickGameList();
      
      if (pw.getGames() != null && !pw.getGames().isEmpty())
      {
         for (PickGame g : pw.getGames())
         {
            this.games.add(new ApiPickGame(g));
         }  
      } else {
         this.complete = false;
      }    
   }

   public String getWeeknumber()
   {
      return weeknumber;
   }

   public void setWeeknumber(String weeknumber)
   {
      this.weeknumber = weeknumber;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public PickGameList getGames()
   {
      return games;
   }

   public void setGames(PickGameList games)
   {
      this.games = games;
   }

   public boolean isComplete()
   {
      return complete;
   }

   public void setComplete(boolean complete)
   {
      this.complete = complete;
   }
   
   
}
