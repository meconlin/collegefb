package com.football.college.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.football.college.domain.Game;
import com.football.college.domain.PickGame;
import com.football.college.domain.PickWeek;
import com.football.college.domain.Week;

/**
 * GamesProcessor
 * 
 * Score Picks made by a user against the master list of Games
 * 
 * TODO
 *  - StrUtils
 *  - Make stats object out of a simple map extension
 *  - Pass in winlose behavior, dont hard code here
 *  - Could the games implement an interface that would make this less specific?
 * 
 * @author mark
 *
 */
public class GamesProcessor
{
   private static final Logger log = Logger.getLogger(GamesProcessor.class.getName());
   protected List<PickWeek>              pickweeks;
   protected List<Week>                  weeks;
   protected Map<String, List<PickWeek>> pickweeksbynumber;
   protected Map<String, Week>           weekbynumber;
   
   //stats
   protected Map<String, Integer>        stats;
   

   public GamesProcessor(List<Week> weeks, List<PickWeek> pickweeks) throws GameProcessException
   {
      if(weeks == null || pickweeks == null)
      {
         log.error("Failed to process PickWeeks : invalid input ");
         throw new GameProcessException("Failed to process PickWeeks : invalide input ");
      }
      
      this.weeks = weeks;
      this.pickweeks = pickweeks;
      stats = new HashMap<String, Integer>();
   }

   public List<PickWeek> process() throws GameProcessException
   {
      log.info("process starting");

      try{
      
      buildLookupMaps();
      checkscores();
      logstats();
      log.info("process finishing");
      
      } catch (Exception e) {
         log.error("Failed to process PickWeeks : " + e.toString());
         throw new GameProcessException(e);
      }
      
      return pickweeks;
   }

   /**
    * logstats
    * 
    */
   private void logstats()
   {
      log.info("Totals from process :");
      log.info("--------------------------------------------------");
      
      for(String key : stats.keySet())
      {
         log.info(padRight(key) + ":" + stats.get(key));
      }
      
      log.info("--------------------------------------------------");
   }
   
   /**
    * checkscores
    * 
    */
   private void checkscores()
   {
      if (weekbynumber != null && !weekbynumber.isEmpty())
      {
         Week currentWeek = null;

         for (String weekkey : weekbynumber.keySet())
         {
            currentWeek = weekbynumber.get(weekkey);
            if (currentWeek != null && currentWeek.isPublished() && pickweeksbynumber.containsKey(weekkey))
            {
               iterateGames(currentWeek.getGames(), pickweeksbynumber.get(weekkey));
            }
         }
      }
   }

   /**
    * iterateGames
    * 
    * @param games
    * @param pickweeks
    */
   private void iterateGames(List<Game> games, List<PickWeek> pickweeks)
   {
      if (games != null && pickweeks != null)
      {
         List<PickGame> pgs = null;
         Set<String> uniqueUsers = new HashSet<String>();
         
         for (PickWeek pw : pickweeks)
         {        
            pgs = pw.getGames();
            uniqueUsers.add(pw.getUsername());
            pw.setCorrect(0);                   //reset pw correct so we dont overscore
            
            for (PickGame pg : pgs)
            {
               incStat("pickgames found");
               
               for (Game g : games)
               {
                  if (this.isSameGame(pg, g))
                  {
                     incStat("games matched to pickgames found");
                     
                     if(g.getWinner() != null && !g.getWinner().isEmpty() && g.getWinner().equals(pg.getPick()))
                     {
                        log.debug("Good Pick Found : " + pw.getNumber() + ":" + pw.getUsername() + ":" + g.getWinner() + ":" + g.getFavorite() + ":" + g.getUnderdog());
                        incStat("good pick found");
                        
                        pg.setPickedcorrect(true);
                        pw.setCorrect(pw.getCorrect()+1);
                        
                     } else if(g.getWinner() != null && !g.getWinner().isEmpty() && pg.getPick() != null && !pg.getPick().isEmpty() && !g.getWinner().equals(pg.getPick()))
                     {
                        log.debug("Bad pick found : " + pw.getNumber() + ":" + pw.getUsername() + ":" + g.getWinner() + ":" + g.getFavorite() + ":" + g.getUnderdog());
                        incStat("bad pick found");
                     } else if(pg.getPick() == null || pg.getPick().isEmpty() && g.getWinner() != null && !g.getWinner().isEmpty()) {
                        log.debug("No pick made ! : " + pw.getNumber() + ":" + pw.getUsername() + ":" + g.getWinner() + ":" + g.getFavorite() + ":" + g.getUnderdog());
                        incStat("no pick found (not published)");
                     } else if(g.getWinner() == null || g.getWinner().isEmpty()) {
                        log.debug("No winner found ! : " + pw.getNumber() + ":" + pw.getUsername() + ":" + g.getWinner() + ":" + g.getFavorite() + ":" + g.getUnderdog());
                        incStat("no winner found");
                     }
                  }
               }
            }
         }
         
         incStat("unique users", uniqueUsers.size());
      }
   }

   /**
    * buildLookupMaps
    * 
    */
   private void buildLookupMaps()
   {
      pickweeksbynumber = new HashMap<String, List<PickWeek>>();
      weekbynumber = new HashMap<String, Week>();

      if (weeks != null && pickweeks != null)
      {
         incStat("weeks found", weeks.size());
         incStat("pickweeks found", pickweeks.size());
         
         for (Week w : weeks)
         {
            weekbynumber.put(w.getNumber(), w);
         }

         for (PickWeek pw : pickweeks)
         {
            if (pickweeksbynumber.containsKey(pw.getNumber()))
            {
               pickweeksbynumber.get(pw.getNumber()).add(pw);
            }
            else
            {
               List<PickWeek> sublist = new ArrayList<PickWeek>();
               sublist.add(pw);
               pickweeksbynumber.put(pw.getNumber(), sublist);
            }
         }
      }
   }

   /**
    * isSameGame
    * 
    * TODO check date too, bowl games etc...
    * 
    * @param regGame
    * @param game
    * @return
    */
   private boolean isSameGame(PickGame regGame, Game game)
   {
      if (regGame != null && game != null)
      {
         if (regGame.getFavorite() != null && game.getFavorite() != null && regGame.getUnderdog() != null && game.getUnderdog() != null && game.prettyDate() != null && regGame.prettyDate() != null)
         {
            if (regGame.getFavorite().equals(game.getFavorite()) && regGame.getUnderdog().equals(game.getUnderdog()) && regGame.prettyDate().equals(game.prettyDate()))
            {
               return true;
            }
         }
      }

      return false;
   }
   
   /**
    * incrementStat
    * 
    * @param stat
    */
   private void incStat(String stat)
   {
      this.incStat(stat, 1);
   }
   
   private void incStat(String stat, int value)
   {
      stats.put(stat, stats.get(stat)!=null?(stats.get(stat)+value):value);
   }
   
   
   private static String padRight(String s)
   {
      return padRight(s, 50);
   }
   
   private static String padRight(String s, int n) {
      return String.format("%1$-" + n + "s", s);  
   }

   


}
