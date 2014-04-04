package com.football.college.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class BaseController
{
   protected String getUsername(){
      SecurityContext sc = SecurityContextHolder.getContext();
      Authentication a = sc.getAuthentication();
      Object principal = a.getPrincipal();
      User u  = (User) principal;
      return u.getUsername();
   }
}
