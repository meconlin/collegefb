package com.football.college.service;

import java.util.List;

import com.football.college.domain.Game;
import com.football.college.domain.PickGame;
import com.football.college.domain.PickWeek;
import com.football.college.domain.Week;
import com.football.college.domain.api.ApiLeaderboard;
import com.football.college.domain.api.ApiPickWeek;
import com.football.college.domain.api.ApiScoreboard;
import com.football.college.domain.api.ApiWeek;


public interface WeekService 
{
   
   /**
    * constructLeaderBoard
    * 
    * @return
    * @throws ServiceException
    */
   public ApiLeaderboard constructLeaderboard() throws ServiceException;
   
   /***
    *        user1  |   user2   |   user3   |
    * game |    val     val         val
    * game |     
    * @throws ServiceException 
    */
   public ApiScoreboard constructScoreboard(String weeknumber) throws ServiceException;
   
   /**
    * scorePickWeeks
    */
   public void scorePickWeeks() throws ServiceException;
   
   /**
    * findAllPickWeeksByUsername
    * 
    * @param username
    * @return
    * @throws ServiceException
    */
   public List<PickWeek> findAllPickWeeksByUsername(String username) throws ServiceException;
   
   /**
    * checkForAdminUpdatesToThisWeek
    * 
    * @param pw
    * @return
    * @throws ServiceException
    */
   public PickWeek checkForAdminUpdatesToThisWeek(PickWeek pw) throws ServiceException;
   
   /**
    * createPickGameFromGame
    * 
    * @param g
    * @return
    * @throws ServiceException
    */
   public PickGame createPickGameFromGame(Game g) throws ServiceException;
   
   /**
    * deletePickWeek
    * 
    * @param pw
    * @throws ServiceException
    */
   public void deletePickWeek(PickWeek pw) throws ServiceException;
   
   /**
    * savePickWeek
    * 
    * @param pw
    * @return
    * @throws ServiceException
    */
   public PickWeek savePickWeek(PickWeek pw) throws ServiceException;
   
   /**
    * submitApiWeekPicks
    * 
    * @param apiWeek
    * @return
    * @throws ServiceException
    */
   public PickWeek submitApiWeekPicks(ApiPickWeek apiWeek) throws ServiceException;
   
   /**
    * findPickWeek
    * 
    * @param weeknumber
    * @param username
    * @return
    * @throws ServiceException
    */
   public PickWeek findPickWeek(String weeknumber, String username) throws ServiceException;
   
   /**
    * @param weeknumber
    * @return
    * @throws ServiceException
    */
   public boolean deleteWeek(String weeknumber) throws ServiceException;
   
   /**
    * findAllWeeks
    * 
    * @param completedOnly
    * @return
    * @throws ServiceException
    */
   public List<Week> findAllWeeks(boolean completedOnly) throws ServiceException;
   
   /**
    * submitApiWeek
    * 
    * @param apiWeek
    * @return
    * @throws ServiceException
    */
   public Week submitApiWeek(ApiWeek apiWeek) throws ServiceException;
   
   /**
    * submitApiWeekScores
    * Save score information from the ApiWeek
    * 
    * @param apiWeek
    * @return
    * @throws ServiceException 
    */
   public Week submitApiWeekScores(ApiWeek apiWeek) throws ServiceException;
   
   /**
    * findWeekByNumber
    * 
    * @param weeknumber
    * @return
    * @throws ServiceException
    */
   public Week findWeekByNumber(String weeknumber) throws ServiceException;
   
}
