package com.football.college.service;

public class ServiceException extends Exception
{
   public enum Type {
      STANDARD;
   }

   protected Type     type;
   protected String   friendlymessage;
   
   public ServiceException(String message, Throwable cause)
   {
      super(message, cause);
      this.init(Type.STANDARD);
   }

   public ServiceException(String message)
   {
      super(message);
      this.init(Type.STANDARD);
   }
   
   public ServiceException(Exception e)
   {
      super(e);
      this.init(Type.STANDARD);
   }
   
   private void init(Type type)
   {
      this.type = type;
   }

   public String getFriendlymessage()
   {
      return friendlymessage;
   }

   public void setFriendlymessage(String friendlymessage)
   {
      this.friendlymessage = friendlymessage;
   }
   
}
