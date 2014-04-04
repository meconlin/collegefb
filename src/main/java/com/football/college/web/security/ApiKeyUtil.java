package com.football.college.web.security;

import java.util.UUID;

public class ApiKeyUtil
{

   public static String createApiKey()
   {
      return UUID.randomUUID().toString();
   }

   
   
   
}
