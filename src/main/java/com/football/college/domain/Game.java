package com.football.college.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.appengine.api.datastore.Key;

/**
 * @author mark
 *
 */
@PersistenceCapable(detachable = "true")
public class Game extends DomainObject
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   private Key key;
   
   @Persistent
   protected String favorite;

   @Persistent
   protected String underdog;

   @Persistent
   protected double spread;
   
   @Persistent
   protected int scorefavored;
   
   @Persistent
   protected int scoreunderdog;
   
   @Persistent
   protected String winner;
   
   @Persistent
   protected Date kickoff;
   
   public Date getKickoff()
   {
      return kickoff;
   }

   public void setKickoff(Date kickoff)
   {
      this.kickoff = kickoff;
   }

   @Persistent
   protected String favoredlocation;
   
   public enum Location {
      HOME, AWAY
   }

   public Key getKey()
   {
      return key;
   }

   public void setKey(Key key)
   {
      this.key = key;
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

   public void setSpread(double d)
   {
      this.spread = d;
   }

   public String getFavoredlocation()
   {
      return favoredlocation;
   }

   public void setFavoredlocation(String favoredlocation)
   {
      this.favoredlocation = favoredlocation;
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

   /************** Helpers *******************************/
   
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(this.kickoff);
      sb.append(" - ");
      sb.append(this.favorite);
      sb.append(" - ");
      sb.append(this.underdog);
      return sb.toString();
   }
   
   public boolean isCompleted()
   {
    
      if((new Date()).after(this.getKickoff()))
      {
         return true;
      } 
      
      return false;
   }
   
   /**
    * Convert a date and time in string format to a Date
    * 
    * @param kickoff
    * @param time
    * @return
    */
   public Date convertDateAndTimeStringToDate(String kickoff, String time)
   {
      DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
      DateTime dt = formatter.parseDateTime( kickoff + " " + time );
      return dt.toDate();
   }
   
   public Long sortableDateTime()
   {
      DateTime dt = new DateTime(this.getKickoff());
      return dt.getMillis();
   }
   
   public String prettyDate()
   {
      DateTime dt = new DateTime(this.getKickoff());
      return dt.toString("MM/dd/yyyy");
   }

   public String prettyTime()
   {
      DateTime dt = new DateTime(this.getKickoff());
      return dt.toString("hh:mm");
   }
   
   public String prettyMilitaryTime()
   {
      DateTime dt = new DateTime(this.getKickoff());
      return dt.toString("HH:mm");
   }
   
}

