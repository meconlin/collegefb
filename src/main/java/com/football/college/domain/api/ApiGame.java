package com.football.college.domain.api;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.football.college.domain.Game;

public class ApiGame
{
   protected String favorite;
   protected String underdog;
   protected double spread;
   protected String gamedate;
   protected String favoredlocation;
   protected TimeLabelValue time;
   protected String timestr;
   protected int scorefavored;
   protected int scoreunderdog;
   protected String winner;
   protected boolean completed;
   protected boolean active;
   protected Long timeinmillis;
   
   public ApiGame()
   {
      
   }
   
   public ApiGame(Game g)
   {
      this.favorite = g.getFavorite();
      this.underdog = g.getUnderdog();
      this.spread = g.getSpread();
      this.gamedate = g.prettyDate();
      this.time = new TimeLabelValue(g.prettyTime(), g.prettyMilitaryTime());
      this.timestr = time.getValue();
      this.favoredlocation = g.getFavoredlocation();
      this.completed = g.isCompleted();
      this.scorefavored = g.getScorefavored();
      this.scoreunderdog = g.getScoreunderdog();
      this.winner = g.getWinner();
      this.active = calcActive(g);
      this.timeinmillis = g.sortableDateTime();
   }

   private boolean calcActive(Game g)
   {
      DateTime dt = new DateTime(g.getKickoff());
      dt.withZone(DateTimeZone.forID("America/New_York"));
      if(dt.isAfterNow() && !completed)
      {
         return true;
      }  
      
      return false;
   }
   
   public String getFavorite()
   {
      return favorite;
   }

   public void setFavorite(String favorite)
   {
      this.favorite = favorite;
   }

   public String getUnderdog()
   {
      return underdog;
   }

   public void setUnderdog(String underdog)
   {
      this.underdog = underdog;
   }

   public double getSpread()
   {
      return spread;
   }

   public void setSpread(double spread)
   {
      this.spread = spread;
   }

   public String getGamedate()
   {
      return gamedate;
   }

   public void setGamedate(String gamedate)
   {
      this.gamedate = gamedate;
   }

   public String getFavoredlocation()
   {
      return favoredlocation;
   }

   public void setFavoredlocation(String favoredlocation)
   {
      this.favoredlocation = favoredlocation;
   }

   public TimeLabelValue getTime()
   {
      return time;
   }

   public void setTime(TimeLabelValue time)
   {
      this.time = time;
   }

   public String getTimestr()
   {
      return timestr;
   }

   public void setTimestr(String timestr)
   {
      this.timestr = timestr;
   }

   public int getScorefavored()
   {
      return scorefavored;
   }

   public void setScorefavored(int scorefavored)
   {
      this.scorefavored = scorefavored;
   }

   public int getScoreunderdog()
   {
      return scoreunderdog;
   }

   public void setScoreunderdog(int scoreunderdog)
   {
      this.scoreunderdog = scoreunderdog;
   }

   public String getWinner()
   {
      return winner;
   }

   public void setWinner(String winner)
   {
      this.winner = winner;
   }

   public boolean isCompleted()
   {
      return completed;
   }

   public void setCompleted(boolean completed)
   {
      this.completed = completed;
   }

   public Long getTimeinmillis()
   {
      return timeinmillis;
   }

   public void setTimeinmillis(Long timeinmillis)
   {
      this.timeinmillis = timeinmillis;
   }
   
}
