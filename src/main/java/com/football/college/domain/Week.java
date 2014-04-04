package com.football.college.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true")
public class Week extends DomainObject
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   private Key key;
   
   @Persistent
   protected String number;

   @Persistent(defaultFetchGroup = "true")
   @Element(dependent = "true")
   protected List<Game> games;
   
   protected boolean complete;
   
   @Persistent
   protected boolean published;
   
   @Persistent
   protected boolean scored;

   public String getNumber()
   {
      return number;
   }

   public void setNumber(String number)
   {
      this.number = number;
   }

   public List<Game> getGames()
   {
      return games;
   }

   public void setGames(List<Game> games)
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

   public boolean isPublished()
   {
      return published;
   }

   public void setPublished(boolean published)
   {
      this.published = published;
   } 
   
   public boolean isScored()
   {
      return scored;
   }

   public void setScored(boolean scored)
   {
      this.scored = scored;
   }

   public Key getKey()
   {
      return key;
   }

   public void setKey(Key key)
   {
      this.key = key;
   }

   /************ Helpers ************/
   
   public void addGame(Game g)
   {
      if(this.getGames() != null)
      {
         this.getGames().add(g);
      } else {
         this.setGames(new ArrayList<Game>());
         this.getGames().add(g);
      }
   }
  
}
