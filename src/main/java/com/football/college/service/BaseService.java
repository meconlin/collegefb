package com.football.college.service;

import java.util.logging.Logger;

public class BaseService
{  
   protected void handleException(Logger log, String message, Exception e) throws ServiceException
   {
      log.severe(log.getName() + ":" + message + ":" + e.toString());
      throw new ServiceException(e);
   }
}
