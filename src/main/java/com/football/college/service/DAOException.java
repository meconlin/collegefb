package com.football.college.service;

public class DAOException extends Exception
{
   public enum Type {
      STANDARD;
   }

   protected Type     type;
   
   public DAOException(String message, Throwable cause)
   {
      super(message, cause);
      this.init(Type.STANDARD);
   }

   public DAOException(String message)
   {
      super(message);
      this.init(Type.STANDARD);
   }
   
   public DAOException(Exception e)
   {
      super(e);
      this.init(Type.STANDARD);
   }
   
   private void init(Type type)
   {
      this.type = type;
   }
}
