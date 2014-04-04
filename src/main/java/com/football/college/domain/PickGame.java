package com.football.college.domain;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author mark
 *
 */
@PersistenceCapable(detachable = "true")
@Inheritance(customStrategy = "complete-table")
public class PickGame extends Game
{   
   public static final String NOT_PICKED = "Not Picked";
   
   @Persistent
   protected String pick;

   @Persistent
   protected boolean pickedcorrect;
   
   
   /******/
   
   public boolean isPickedcorrect()
   {
      return pickedcorrect;
   }

   public void setPickedcorrect(boolean pickedcorrect)
   {
      this.pickedcorrect = pickedcorrect;
   }

   public String getPick()
   {
      return pick;
   }

   public void setPick(String pick)
   {
      if (pick != null && pick.equals(this.getFavorite()) || pick.equals(this.getUnderdog()))
      {
         this.pick = pick;
      }
   }

}
