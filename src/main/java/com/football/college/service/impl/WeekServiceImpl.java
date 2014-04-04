package com.football.college.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.football.college.domain.Game;
import com.football.college.domain.PickGame;
import com.football.college.domain.PickWeek;
import com.football.college.domain.Week;
import com.football.college.domain.api.ApiGame;
import com.football.college.domain.api.ApiLeaderboard;
import com.football.college.domain.api.ApiPickGame;
import com.football.college.domain.api.ApiPickWeek;
import com.football.college.domain.api.ApiScoreboard;
import com.football.college.domain.api.ApiWeek;
import com.football.college.service.BaseService;
import com.football.college.service.DAOException;
import com.football.college.service.FootballUserService;
import com.football.college.service.GameProcessException;
import com.football.college.service.GamesProcessor;
import com.football.college.service.ServiceException;
import com.football.college.service.WeekService;
import com.football.college.service.dao.WeekDAO;

public class WeekServiceImpl extends BaseService implements WeekService
{
   private static final Logger log = Logger.getLogger(WeekServiceImpl.class.getName());

   @Autowired
   private WeekDAO             weekDao;

   @Autowired
   private FootballUserService userService;

   public void setUserService(FootballUserService userService)
   {
      this.userService = userService;
   }

   public void deletePickWeek(PickWeek pw) throws ServiceException
   {
      try
      {
         weekDao.delete(pw);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to delete pick week", e);
      }
   }

   public PickWeek savePickWeek(PickWeek pw) throws ServiceException
   {
      try
      {
         pw = weekDao.save(pw);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save pick week", e);
      }

      return pw;
   }

   /**
    * constructLeaderboard
    * 
    * @return
    * @throws ServiceException
    */
   public ApiLeaderboard constructLeaderboard() throws ServiceException
   {
      ApiLeaderboard leaderboard = new ApiLeaderboard();
      List<String> playerNames = userService.findAllPlayers();
      List<String> values = new ArrayList<String>();
      Map<String, Integer> playerTotalScore = new HashMap<String, Integer>();
      PickWeek pw = null;
      int weekcount = this._findMaxCompletedPickWeek();
      DecimalFormat df2 = new DecimalFormat("##0.00");

      if (weekcount > 0)
      {
         // Sort Usernames
         //
         Collections.sort(playerNames);

         // for each game, find each users pick 
         //
         for (String player : playerNames)
         {
            leaderboard.addColumn(player);
            playerTotalScore.put(player, 0); //init correct score count
         }

         // For each possible week, find users correctly picked number
         //
         for (int i = 1; i <= weekcount; i++)
         {
            values = new ArrayList<String>();
            for (String playerName : playerNames)
            {
               pw = this.findPickWeek(i + "", playerName);
               values.add(pw.getCorrect() + "");
               playerTotalScore.put(playerName, playerTotalScore.get(playerName) + pw.getCorrect());
            }

            leaderboard.addRow(i, values);
         }

         // place calculated score in list (in order) on the leaderboard
         List<Integer> scores = new ArrayList<Integer>();
         List<String> averages = new ArrayList<String>();
         for (String player : playerNames)
         {
            scores.add(playerTotalScore.get(player));
            averages.add(new Double(df2.format(playerTotalScore.get(player) / weekcount)).doubleValue() + "");
         }

         leaderboard.addAverages(averages);
         leaderboard.addTotals(scores);
      }

      return leaderboard;
   }

   /***
    *        user1  |   user2   |   user3   |
    * game |    val     val         val
    * game |     
    * @throws ServiceException 
    */
   public ApiScoreboard constructScoreboard(String weeknumber) throws ServiceException
   {
      // find games in order of time/date
      ApiScoreboard scoreboard = new ApiScoreboard();
      Week week = this.findWeekByNumber(weeknumber);
      List<Game> games = week.getGames();
      List<String> playerNames = userService.findAllPlayers();
      PickWeek pw = null;
      List<String> values = new ArrayList<String>();
      Map<String, Integer> playerScore = new HashMap<String, Integer>();

      // Sort Games by Kickoff Date
      //
      Collections.sort(games, new Comparator<Game>()
         {
            @Override
            public int compare(Game one, Game two)
            {
               return one.getKickoff().compareTo(two.getKickoff());
            }
         });

      // Sort Usernames
      //
      Collections.sort(playerNames);

      // for each game, find each users pick 
      //
      for (String player : playerNames)
      {
         scoreboard.addColumn(player);
         playerScore.put(player, 0); //init correct score count
      }

      // need to keep track of players total correct
      //

      for (Game g : games)
      {
         ApiGame apig = new ApiGame(g);
         values = new ArrayList<String>();
         for (String player : playerNames)
         {
            pw = this.findPickWeek(weeknumber, player);
            String pick = this.pickFromPickWeek(pw, g);

            if (pick == null)
            {
               pick = "";
            }
            else
            {
               // if user picked correct, increment score
               if (pick.equals(apig.getWinner()))
               {
                  playerScore.put(player, playerScore.get(player) + 1);
               }
            }

            values.add(pick);
         }

         scoreboard.addRow(apig, values);
      }

      // place calculated score in list (in order) on the scoreboard
      List<Integer> scores = new ArrayList<Integer>();
      for (String player : playerNames)
      {
         scores.add(playerScore.get(player));
      }

      scoreboard.addTotals(scores);
      return scoreboard;
   }

   /**
    * scorePickWeeks
    * 
    * @param g
    * @return
    * @throws ServiceException
    */
   public void scorePickWeeks() throws ServiceException
   {
      log.fine("weekService : scorepickweeks : START");
      try
      {
         List<PickWeek> pickweeks = this._findAllPickWeeks();
         List<Week> weeks = this.findAllWeeks(false);
         GamesProcessor gp = new GamesProcessor(weeks, pickweeks);
         List<PickWeek> results = gp.process();

         //Save updated scores
         //
         if (results != null && !results.isEmpty())
         {
            for (PickWeek pw : results)
            {
               this.savePickWeek(pw);
            }
         }

      }
      catch (ServiceException e)
      {
         handleException(log, "Failure to score pick weeks", e);
      }
      catch (GameProcessException e)
      {
         handleException(log, "Failure to score pick weeks", e);
      }

      log.fine("weekService : scorepickweeks : END");
   }

   /**
    * createPickGameFromGame
    * 
    * @param g
    * @return
    * @throws ServiceException
    */
   public PickGame createPickGameFromGame(Game g) throws ServiceException
   {
      PickGame newpg = new PickGame();
      newpg.setFavorite(g.getFavorite());
      newpg.setUnderdog(g.getUnderdog());
      newpg.setSpread(g.getSpread());
      newpg.setKickoff(g.getKickoff());
      newpg.setFavoredlocation(g.getFavoredlocation());
      return newpg;
   }

   /**
    * checkForAdminUpdatesToThisWeek
    * 
    * any new games?
    * any games removed?
    * any games update (spread, home/away, time?)
    * 
    * @param pw
    * @return
    * @throws ServiceException
    */
   public PickWeek checkForAdminUpdatesToThisWeek(PickWeek pw) throws ServiceException
   {
      List<PickGame> pickGames = null;
      List<PickGame> pickGamesCopy = null;
      List<PickGame> notfound = null;
      pw.getGames();
      Week w = findWeekByNumber(pw.getNumber());
      boolean foundgame = false;

      // loop all games in week
      // compare to existing list
      // add new ones
      // check for updates to spread, home away, and time on all others
      //
      if (pw != null && pw.getGames() != null && w != null && w.getGames() != null)
      {
         pickGames = pw.getGames();

         // look for new games and updates in the Week (admin created and official list)
         //
         for (Game g : w.getGames())
         {
            foundgame = false;
            for (PickGame pg : pw.getGames())
            {
               // do games match?
               // update all updatable fields
               if (isSameGame(pg, g))
               {
                  pg.setSpread(g.getSpread());
                  pg.setKickoff(g.getKickoff());
                  pg.setFavoredlocation(g.getFavoredlocation());
                  foundgame = true;
               }
            }

            if (!foundgame) //not found add as new game
            {
               pickGames.add(this.createPickGameFromGame(g));
            }
         }

         //  look for pickweek games that have no match in the official week (Admin removed)
         //
         pickGamesCopy = new ArrayList<PickGame>();
         notfound = new ArrayList<PickGame>();
         for (PickGame pg : pw.getGames())
         {
            foundgame = false;
            for (Game g : w.getGames())
            {
               if (isSameGame(pg, g))
               {
                  foundgame = true;
               }
            }

            if (foundgame)
            {
               pickGamesCopy.add(pg);
            }
            else
            {
               notfound.add(pg);
            }
         }

      }

      pw.setGames(pickGamesCopy);

      // remove the ones not found
      if (notfound != null)
      {
         for (PickGame pg : notfound)
         {
            this._deletePickGame(pg);
         }
      }

      return pw;
   }

   /**
    * submitApiWeekPicks
    * 
    * @param apiWeek
    * @return
    * @throws ServiceException
    */
   public PickWeek submitApiWeekPicks(ApiPickWeek apiWeek) throws ServiceException
   {
      PickWeek pw = new PickWeek();

      // look up week by number
      //
      pw = this.findPickWeek(apiWeek.getWeeknumber(), apiWeek.getUsername());

      if (apiWeek.getGames() != null && pw != null && pw.getGames() != null)
      {
         for (ApiPickGame g : apiWeek.getGames())
         {
            for (PickGame regPickGame : pw.getGames())
            {
               if (isSameGame(regPickGame, g))
               {
                  regPickGame.setPick(g.getPick());
                  break;
               }
            }
         }
      }

      try
      {
         pw = weekDao.save(pw);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save pick week", e);
      }

      return pw;
   }

   /**
    * findAllPickWeeksByUsername
    * 
    * @param username
    * @return
    * @throws ServiceException
    */
   public List<PickWeek> findAllPickWeeksByUsername(String username) throws ServiceException
   {
      try
      {
         List<PickWeek> listofpickweeks = weekDao.loadMany(PickWeek.class, username, "username", "number");
         return listofpickweeks;
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to findPickWeek by username ", e);
      }

      return null;
   }

   private void _deletePickGame(PickGame pg) throws ServiceException
   {
      try
      {
         weekDao.delete(pg);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to remove PickGame", e);
      }
   }

   /**
    * _findAllPickWeeks
    * 
    * @return
    * @throws ServiceException
    */
   public List<PickWeek> _findAllPickWeeks() throws ServiceException
   {
      try
      {
         List<PickWeek> listofpickweeks = weekDao.loadMany(PickWeek.class, "number");
         return listofpickweeks;
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to findPickWeeks ", e);
      }

      return null;
   }

   /**
    * @return
    * @throws ServiceException
    */
   public int _findMaxCompletedPickWeek() throws ServiceException
   {
      List<PickWeek> listofpickweeks = this._findAllPickWeeks();
      int max = 0;

      for (PickWeek pw : listofpickweeks)
      {
         Week w = findWeekByNumber(pw.getNumber());
         ApiWeek apiWeek = new ApiWeek(w);
         if (apiWeek.isActive())
         {
            if (Integer.parseInt(pw.getNumber()) > max)
            {
               max = Integer.parseInt(pw.getNumber());
            }
         }

      }

      return max;
   }

   /****
    * findPickWeek
    * 
    * @param weeknumber
    * @param username
    * @return
    * @throws ServiceException
    */
   public PickWeek findPickWeek(String weeknumber, String username) throws ServiceException
   {
      PickWeek pw = null;

      try
      {
         pw = weekDao.load(PickWeek.class, weeknumber, "number", username, "username");

         //EMPTY BUILD IT FROM WEEK
         //
         if (pw == null || pw.getGames() == null || pw.getGames().isEmpty())
         {
            if (pw == null)
            {
               pw = new PickWeek();
            }

            //get the week
            Week w = findWeekByNumber(weeknumber);

            //is it there ? is it published
            //
            if (w != null && w.isPublished())
            {
               pw.setNumber(w.getNumber());
               pw.setUsername(username);

               if (w.getGames() != null && !w.getGames().isEmpty())
               {
                  for (Game g : w.getGames())
                  {
                     pw.addGame(this.createPickGameFromGame(g));
                  }
               }

               pw = savePickWeek(pw);

            }
            else
            {
               log.warning("Failure to get pick week, that week is not published : " + weeknumber);
            }

         }

         // ANY UPDATES ?
         //
         if (pw != null)
         {
            pw = this.checkForAdminUpdatesToThisWeek(pw);
         }
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to findPickWeek ", e);
      }

      return pw;
   }

   /**
    * @param weeknumber
    * @return
    * @throws ServiceException
    */
   public boolean deleteWeek(String weeknumber) throws ServiceException
   {
      Week w = null;
      try
      {
         w = weekDao.load(Week.class, weeknumber, "number");

         // delete related pickweeks
         //
         List<PickWeek> pws = this._findAllPickWeeks();
         for (PickWeek pw : pws)
         {
            if (weeknumber.equals(pw.getNumber()))
            {
               this.deletePickWeek(pw);
            }
         }

         if (w != null)
         {
            weekDao.delete(w);
         }
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to deleteWeek ", e);
         return false;
      }

      return true;
   }

   /**
    * findAllWeeks
    * Optinally skip ones that are complete 
    * 
    * @param completedOnly
    * @return
    */
   public List<Week> findAllWeeks(boolean completedOnly) throws ServiceException
   {
      List<Week> weeks = null;
      List<Week> finalweeks = null;

      try
      {
         weeks = weekDao.loadMany(Week.class);

         if (completedOnly && weeks != null)
         {
            finalweeks = new ArrayList<Week>();

            for (Week w : weeks)
            {
               if (w.isComplete())
               {
                  finalweeks.add(w);
               }
            }
         }
         else
         {
            finalweeks = weeks;
         }
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to load weeks ", e);
      }

      return finalweeks;
   }

   /* (non-Javadoc)
    * @see com.football.college.service.WeekService#findWeekByNumber(java.lang.String)
    */
   public Week findWeekByNumber(String weeknumber) throws ServiceException
   {
      Week w = null;
      try
      {
         if (Integer.parseInt(weeknumber) > 0)
         {
            w = weekDao.load(Week.class, weeknumber, "number");
         }
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to findWeekByNumber ", e);
      }

      return w;
   }

   /**
    * submitApiWeekScores
    * Save score information from the ApiWeek
    * 
    * @param apiWeek
    * @return
    * @throws ServiceException 
    */
   public Week submitApiWeekScores(ApiWeek apiWeek) throws ServiceException
   {
      Week w = new Week();

      // look up week by number
      //
      w = this.findWeekByNumber(apiWeek.getWeeknumber());

      if (apiWeek.getGames() != null && w != null && w.getGames() != null)
      {
         for (ApiGame g : apiWeek.getGames())
         {
            for (Game regGame : w.getGames())
            {
               if (isSameGame(regGame, g))
               {
                  regGame.setScorefavored(g.getScorefavored());
                  regGame.setScoreunderdog(g.getScoreunderdog());
                  regGame.setWinner(g.getWinner());
                  break;
               }
            }
         }
      }

      try
      {
         w = weekDao.save(w);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save week", e);
      }

      return w;
   }

   /**
    * submitApiWeek
    * Build for submit from week construction screen
    * 
    * @param apiWeek
    * @return
    * @throws ServiceException 
    */
   public Week submitApiWeek(ApiWeek apiWeek) throws ServiceException
   {
      Week w = new Week();

      // look up week by number
      //
      w = this.findWeekByNumber(apiWeek.getWeeknumber());

      if (w == null)
      {
         w = new Week();
         w.setNumber(apiWeek.getWeeknumber());
      }

      w.setPublished(apiWeek.isPublished());

      if (apiWeek.getGames() != null)
      {
         ArrayList<Game> games = new ArrayList<Game>();
         for (ApiGame g : apiWeek.getGames())
         {
            Game regGame = new Game();
            regGame.setFavorite(g.getFavorite());
            regGame.setUnderdog(g.getUnderdog());
            regGame.setKickoff(regGame.convertDateAndTimeStringToDate(g.getGamedate(), g.getTimestr()));
            regGame.setFavoredlocation(g.getFavoredlocation());
            regGame.setSpread(g.getSpread());
            regGame.setScorefavored(g.getScorefavored());
            regGame.setScoreunderdog(g.getScoreunderdog());
            regGame.setWinner(g.getWinner());
            games.add(regGame);
         }

         w.setGames(games);
      }

      try
      {
         w = weekDao.save(w);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save week", e);
      }

      return w;
   }

   /**
    * isSameGame
    * 
    * @param regGame
    * @param apiGame
    * @return
    */
   private boolean isSameGame(Game regGame, ApiGame apiGame)
   {
      if (regGame != null && apiGame != null)
      {
         if (regGame.getFavorite() != null && apiGame.getFavorite() != null && regGame.getUnderdog() != null && apiGame.getUnderdog() != null)
         {
            if (regGame.getFavorite().equals(apiGame.getFavorite()) && regGame.getUnderdog().equals(apiGame.getUnderdog()))
            {
               return true;
            }
         }
      }

      return false;
   }

   /**
    * isSameGame
    * 
    * @param regGame
    * @param game
    * @return
    */
   private boolean isSameGame(PickGame regGame, Game game)
   {
      if (regGame != null && game != null)
      {
         if (regGame.getFavorite() != null && game.getFavorite() != null && regGame.getUnderdog() != null && game.getUnderdog() != null)
         {
            if (regGame.getFavorite().equals(game.getFavorite()) && regGame.getUnderdog().equals(game.getUnderdog()))
            {
               return true;
            }
         }
      }

      return false;
   }

   /**
    * isSameGame
    * 
    * @param regGame
    * @param apiGame
    * @return
    */
   private boolean isSameGame(PickGame regGame, ApiPickGame apiGame)
   {
      if (regGame != null && apiGame != null)
      {
         if (regGame.getFavorite() != null && apiGame.getFavorite() != null && regGame.getUnderdog() != null && apiGame.getUnderdog() != null)
         {
            if (regGame.getFavorite().equals(apiGame.getFavorite()) && regGame.getUnderdog().equals(apiGame.getUnderdog()))
            {
               return true;
            }
         }
      }

      return false;
   }

   /**
    * pickFromPickWeek
    * 
    * @param pw
    * @param g
    * @return
    */
   private String pickFromPickWeek(PickWeek pw, Game g)
   {
      String answer = PickGame.NOT_PICKED;

      for (PickGame pg : pw.getGames())
      {
         if (isSameGame(pg, g))
         {
            answer = pg.getPick();
            break;
         }
      }

      return answer;
   }

}
