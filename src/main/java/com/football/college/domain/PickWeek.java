package com.football.college.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * @author mark
 *
 */
@PersistenceCapable(detachable = "true")
public class PickWeek extends DomainObject
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   private Key key;
   
   @Persistent
   protected String number;

   @Persistent
   protected String username;
   
   @Persistent
   protected int correct;
   
   @Persistent(defaultFetchGroup = "true")
   @Element(dependent = "true")
   protected List<PickGame> games;
   

   public Key getKey()
   {
      return key;
   }

   public void setKey(Key key)
   {
      this.key = key;
   }

   public String getNumber()
   {
      return number;
   }

   public void setNumber(String number)
   {
      this.number = number;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public List<PickGame> getGames()
   {
      return games;
   }

   public void setGames(List<PickGame> games)
   {
      this.games = games;
   }

   public int getCorrect()
   {
      return correct;
   }

   public void setCorrect(int correct)
   {
      this.correct = correct;
   }

   /************ Helpers ************/
   
   public void addGame(PickGame g)
   {
      if(this.getGames() != null)
      {
         this.getGames().add(g);
      } else {
         this.setGames(new ArrayList<PickGame>());
         this.getGames().add(g);
      }
   }
}
