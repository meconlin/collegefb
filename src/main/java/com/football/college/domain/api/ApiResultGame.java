package com.football.college.domain.api;

import java.util.List;

import org.joda.time.DateTime;

import com.football.college.domain.Game;
import org.joda.time.DateTimeZone;

public class ApiResultGame
{
   protected String favorite;
   protected String underdog;
   protected double spread;
   protected String gamedate;
   protected String favoredlocation;
   protected TimeLabelValue time;
   protected int scorefavored;
   protected int scoreunderdog;
   protected String winner;
   protected boolean completed;
   protected boolean active;
   
   protected List<String> userpicks;
   
   public ApiResultGame()
   {
      
   }
   
   public ApiResultGame(Game g )
   {
      this.favorite = g.getFavorite();
      this.underdog = g.getUnderdog();
      this.spread = g.getSpread();
      this.gamedate = g.prettyDate();
      this.time = new TimeLabelValue(g.prettyTime(), g.prettyMilitaryTime());
      this.favoredlocation = g.getFavoredlocation();
      this.completed = g.isCompleted();
      this.scorefavored = g.getScorefavored();
      this.scoreunderdog = g.getScoreunderdog();
      this.winner = g.getWinner();
      this.active = calcActive(g);
      
   }

   private boolean calcActive(Game g)
   {
      DateTime dt = new DateTime(g.getKickoff());
      DateTime ny = dt.withZone(DateTimeZone.forID("America/New_York"));
      if(ny.isAfterNow() && !completed)
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
   
  
}
