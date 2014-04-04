package com.football.college.domain.api;

import com.football.college.domain.PickGame;

public class ApiPickGame
{
   protected String favorite;
   protected String underdog;
   protected String pick;
   protected double spread;
   protected Long timeinmillis;
   protected String gamedate;
   protected String favoredlocation;
   protected TimeLabelValue time;

   public ApiPickGame()
   {
      
   }
   
   public ApiPickGame(PickGame g)
   {
      this.favorite = g.getFavorite();
      this.underdog = g.getUnderdog();
      this.spread = g.getSpread();
      this.gamedate = g.prettyDate();
      this.time = new TimeLabelValue(g.prettyTime(), g.prettyMilitaryTime());
      this.favoredlocation = g.getFavoredlocation();
      this.pick = g.getPick();
      this.timeinmillis = g.sortableDateTime();
   }
   
   public String getPick()
   {
      return pick;
   }

   public void setPick(String pick)
   {
      this.pick = pick;
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

   public void setTimeinmillis(Long timeinmillis)
   {
      this.timeinmillis = timeinmillis;
   }

   public Long getTimeinmillis()
   {
      return timeinmillis;
   }

  
}
