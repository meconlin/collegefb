package com.football.college.domain.api;

import java.util.Date;

import org.joda.time.DateTime;

import com.football.college.domain.Game;
import com.football.college.domain.GameList;
import com.football.college.domain.Week;

/**
 * ApiWeek
 * 
 * 
 * @author mark
 */
public class ApiWeek
{
   protected String        weeknumber;
   protected GameList      games;
   protected boolean       complete;            //derived
   protected boolean       published;
   protected boolean       scored;
   protected boolean       active;              //derived
   protected int           numberofgames;       //derived
   
   protected String        winner;
   protected int           winnercount;
   protected String        loser;
   protected int           losercount;
   protected float         average;
   
   protected String        daterange;
   protected String        mindate;
   protected String        maxdate;
   
   private Date currentmindate;
   private Date currentmaxdate;

   /** specific information for a user week **/
   
   private int userscore;
   
   
   public ApiWeek()
   {

   }

   //TODO week results
   //public ApiWeek(Week w, WeekResult wr, UserWeekResult uwr)
   //{
   //   
   //}
   
   public ApiWeek(Week w)
   {
      this.weeknumber = w.getNumber();
      this.complete = w.isComplete();
      this.published = w.isPublished();
      this.games = new GameList();
     
      if (w.getGames() != null && !w.getGames().isEmpty())
      {
         boolean foundonenotcomplete = false;
         this.numberofgames = w.getGames().size();
         
         for (Game g : w.getGames())
         {
            this.calcMinMaxDate(g.getKickoff());
            
            if(!g.isCompleted())
               foundonenotcomplete = true;
            
            this.games.add(new ApiGame(g));
         }
         
         // we found one not complete, so the week is not complete
         //
         if(!foundonenotcomplete)
            this.complete = true;
         
      } else {
         this.complete = false;
      }
      
      this.calcActive();
   }

   /*************************/

   /**
    * daterange
    * 
    * @return
    */
   public String daterangeStr()
   {
      return mindateStr() + " - " + maxdateStr();
   }
   
   /**
    * mindate
    * 
    * @return
    */
   public String mindateStr()
   {
      if(currentmindate != null)
      {
         DateTime dt = new DateTime(currentmindate);
         return dt.toString("MM/dd/yyyy hh:mm");
      }
      return "";
   }
   
   /**
    * maxdate
    * 
    * @return
    */
   public String maxdateStr()
   {
      if(currentmaxdate != null)
      {
         DateTime dt = new DateTime(currentmaxdate);
         return dt.toString("MM/dd/yyyy hh:mm");
      }
      return "";
   }
   
   private void calcActive()
   {
      DateTime dtmin = new DateTime(currentmindate);
      if(dtmin.isBeforeNow())
      {
         active = true;
      }  
   }
   
   private void calcMinMaxDate(Date d) 
   {
      if(currentmindate != null)
      {
         if(currentmindate.compareTo(d) > 0)
         {
           this.currentmindate = d; 
         } 
      } else {
         this.currentmindate = d;
      }
      
      if(currentmaxdate != null)
      {
         if(currentmaxdate.compareTo(d) < 0)
         {
           this.currentmaxdate = d; 
         } 
      } else {
         this.currentmaxdate = d;
      }
      
      this.mindate = this.mindateStr();
      this.maxdate = this.maxdateStr();
      this.daterange = this.daterangeStr();
   }
   
   
   /************** GETTERS AND SETTERS *******************/
   
   
   
   public String getWeeknumber()
   {
      return weeknumber;
   }

   public boolean isScored()
   {
      return scored;
   }

   public void setScored(boolean scored)
   {
      this.scored = scored;
   }

   public String getDaterange()
   {
      return daterange;
   }

   public String getMindate()
   {
      return mindate;
   }

   public String getMaxdate()
   {
      return maxdate;
   }

   public void setWeeknumber(String weeknumber)
   {
      this.weeknumber = weeknumber;
   }

   public GameList getGames()
   {
      return games;
   }

   public void setGames(GameList games)
   {
      this.games = games;
   }

   public boolean isComplete()
   {
      return complete;
   }

   /*public void setComplete(boolean complete)
   {
      this.complete = complete;
   }*/

   public boolean isPublished()
   {
      return published;
   }

   public void setPublished(boolean published)
   {
      this.published = published;
   }

   public int getNumberofgames()
   {
      return numberofgames;
   }

   public void setNumberofgames(int numberofgames)
   {
      this.numberofgames = numberofgames;
   }

   public String getWinner()
   {
      return winner;
   }

   public void setWinner(String winner)
   {
      this.winner = winner;
   }

   public int getWinnercount()
   {
      return winnercount;
   }

   public void setWinnercount(int winnercount)
   {
      this.winnercount = winnercount;
   }

   public String getLoser()
   {
      return loser;
   }

   public void setLoser(String loser)
   {
      this.loser = loser;
   }

   public int getLosercount()
   {
      return losercount;
   }

   public void setLosercount(int losercount)
   {
      this.losercount = losercount;
   }

   public float getAverage()
   {
      return average;
   }

   public void setAverage(float average)
   {
      this.average = average;
   }

   public int getUserscore()
   {
      return userscore;
   }

   public void setUserscore(int userscore)
   {
      this.userscore = userscore;
   }   
   
   public boolean isActive()
   {
      return active;
   }
   
}
