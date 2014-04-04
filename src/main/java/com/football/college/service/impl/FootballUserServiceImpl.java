package com.football.college.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.football.college.domain.ApiKeyUserName;
import com.football.college.domain.User;
import com.football.college.service.BaseService;
import com.football.college.service.DAOException;
import com.football.college.service.FootballUserService;
import com.football.college.service.ServiceException;
import com.football.college.service.dao.UserDAO;

public class FootballUserServiceImpl extends BaseService implements FootballUserService, UserDetailsService, InitializingBean
{
   private static final Logger log = Logger.getLogger(FootballUserServiceImpl.class.getName());
   
   @Autowired
   private UserDAO             userDao;   
   private  @Value("#{myproperties.adminuser}") String adminusername; 
   private  @Value("#{myproperties.adminpassword}") String adminuserpassword; 
   
   @Override
   public void afterPropertiesSet() throws Exception
   {
      // setup admin user if it doesnt exist
      User admin = this.load(adminusername);
      if(admin == null)
      {
         admin = new User(adminusername, adminuserpassword, User.Role.ROLE_ADMIN);
         this.save(admin);
      }
   }
   
   /**
    * findAllPlayers
    * 
    * @return
    * @throws ServiceException
    */
   public List<String> findAllPlayers() throws ServiceException
   {
      List<User> users = this.loadUsers();
      List<String> playernames = new ArrayList<String>();

      for (User u : users)
      {
         if (!User.ADMIN.equals(u.getUsername()))
         {
            playernames.add(u.getUsername());
         }
      }

      return playernames;
   }

   @Override
   public boolean delete(Long userid) throws ServiceException
   {
      User u = null;
      try
      {
         u = this.load(userid);
         if (u != null)
         {
            userDao.delete(u);
         }
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to delete User ", e);
         return false;
      }
      
      return true;
   }

   @Override
   public User load(String username) throws ServiceException
   {
      try
      {
         return userDao.load(User.class, username, "username");
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to load user", e);
      }

      return null;
   }

   @Override
   public User load(Long userid) throws ServiceException
   {
      try
      {
         return userDao.load(User.class, userid);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to load user", e);
      }

      return null;
   }

   @Override
   public List<User> loadUsers() throws ServiceException
   {
      try
      {
         return userDao.loadMany(User.class);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to load users", e);
      }

      return null;
   }

   @Override
   public User save(User user) throws ServiceException
   {
      try
      {
         User u = this.load(user.getUsername());
         if (u != null)
         {
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            return userDao.save(u);
         }
         else
         {
            return userDao.save(user);
         }

      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save users", e);
      }
      return null;
   }

   public User lookup(String username, String password) throws ServiceException
   {
      try
      {
         User u = this.load(username);
         if (u != null && password.equals(u.getPassword()))
         {
            return u;
         }
      }
      catch (ServiceException e)
      {
         handleException(log, "Login failure for : " + username, e);
      }

      return null;
   }

   /************************************************
    * User - Api Key Relationship
    ************************************************/

   /**
    * @param apikeyuser
    * @return
    * @throws ServiceException
    */
   public ApiKeyUserName save(ApiKeyUserName apikeyuser) throws ServiceException
   {
      try
      {
         return userDao.save(apikeyuser);
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save apikey - user", e);
      }
      return null;
   }

   /**
    * @param apikey
    * @return
    * @throws ServiceException
    */
   public User findUserByActiveApiKey(String apikey) throws ServiceException
   {
      try
      {
         User u = null;
         ApiKeyUserName key = userDao.load(ApiKeyUserName.class, apikey, "apikey");
         if (key != null && key.stillActive())
         {
            u = userDao.load(User.class, key.getUsername(), "username");
            u.setPassword(null);
         }

         return u;
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save apikey - user", e);
      }
      return null;
   }

   /**
    * @param apikey
    * @return
    * @throws ServiceException
    */
   public ApiKeyUserName findUserWithActiveApiKeyByUsername(String username) throws ServiceException
   {
      try
      {
         return userDao.load(ApiKeyUserName.class, username, "username");
      }
      catch (DAOException e)
      {
         handleException(log, "Failure to save apikey - user", e);
      }
      return null;
   }

   /************************************************
    * UserDetails impls for Spring Security 
    ************************************************/

   @Override
   public UserDetails getUserByApiKey(String apiKey)
   {

      //TODO find if user is logged in 

      UserDetails userDetails = loadUserByUsername("mconlin");

      if (userDetails == null)
      {
         throw new UsernameNotFoundException("User could not be found with the supplied api key.");
      }

      return userDetails;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
   {

      try
      {
         User domainUser = this.load(username);

         boolean enabled = true;
         boolean accountNonExpired = true;
         boolean credentialsNonExpired = true;
         boolean accountNonLocked = true;

         return new org.springframework.security.core.userdetails.User(domainUser.getUsername(), domainUser.getPassword().toLowerCase(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(domainUser.getRole()));

      }
      catch (Exception e)
      {
         log.severe("username not valid : " + username);
         throw new RuntimeException(e);
      }
   }

   public Collection<? extends GrantedAuthority> getAuthorities(String role)
   {
      List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
      return authList;
   }

   public List<String> getRoles(String role)
   {
      List<String> roles = new ArrayList<String>();

      if (User.Role.ROLE_ADMIN.name().equals(role))
      {
         roles.add("ROLE_USER");
         roles.add("ROLE_ADMIN");

      }
      else if (User.Role.ROLE_USER.name().equals(role))
      {
         roles.add("ROLE_USER");
      }

      return roles;
   }

   public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles)
   {
      List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
      for (String role : roles)
      {
         authorities.add(new SimpleGrantedAuthority(role));
      }
      return authorities;
   }

}
