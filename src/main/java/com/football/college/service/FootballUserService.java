package com.football.college.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.football.college.domain.ApiKeyUserName;
import com.football.college.domain.User;

public interface FootballUserService {

   /**
    * findAllPlayers
    * 
    * @return
    * @throws ServiceException
    */
   public List<String> findAllPlayers() throws ServiceException;
   
	public User save(User user) throws ServiceException;
	
	public User load(String username) throws ServiceException;
	
	public User load(Long userId) throws ServiceException;
	
	public boolean delete(Long userid) throws ServiceException;
	
	public List<User> loadUsers() throws ServiceException;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	public UserDetails getUserByApiKey(String apiKey);
	
	public User lookup(String username, String password) throws ServiceException;
	
	public ApiKeyUserName save(ApiKeyUserName apikeyuser) throws ServiceException;
	
	public User findUserByActiveApiKey(String apikey) throws ServiceException;
	
	public ApiKeyUserName findUserWithActiveApiKeyByUsername(String username) throws ServiceException;
}
