package com.football.college.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.football.college.web.UserContainer;
import com.football.college.web.security.token.ApiKeyAuthenticationToken;


public class ApiKeyAuthenticationFilter extends AbstractAuthenticationProcessingFilter
{
   private static final Logger log = Logger.getLogger(ApiKeyAuthenticationFilter.class.getName());
   private static final String API_KEY_PARAMETER_NAME = "apikey";

   protected ApiKeyAuthenticationFilter(String defaultFilterProcessesUrl)
   {
      super(defaultFilterProcessesUrl);
   }
   
   private boolean hasExpired(UserContainer u)
   {
      return true;
   }

   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException
   {
      String apiKeyValue = obtainAPIKeyValue(request);
      log.info("attemptAuthentication : start : apikey - " + apiKeyValue);
      log.info("attemptAuthentication : start : request - " + request.getRequestURI());
      //apiKeyValue = "testapikey";
      
      AbstractAuthenticationToken authRequest = createAuthenticationToken(apiKeyValue, null);

      // Allow subclasses to set the "details" property
      authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
      log.info("attemptAuthentication : end");
      return this.getAuthenticationManager().authenticate(authRequest);
   }

   protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                           Authentication authResult) throws IOException, ServletException {
       super.successfulAuthentication(request, response, chain, authResult);
       chain.doFilter(request, response);
   }
   
   /**
    * @param apiKeyValue
    * @param credentials
    * @return
    */
   private AbstractAuthenticationToken createAuthenticationToken(String apiKeyValue, Object credentials) {
      return new ApiKeyAuthenticationToken(apiKeyValue,credentials);
  }
   
   /**
    * @param request
    * @return
    * @throws UnsupportedEncodingException
    */
   private String obtainAPIKeyValue(HttpServletRequest request) throws UnsupportedEncodingException
   {
      String param = getParameterValue(request, API_KEY_PARAMETER_NAME);
      return param;
   }


   /**
    * @param request
    * @param requestParameterName
    * @return
    */
   private String getParameterValue(HttpServletRequest request, String requestParameterName)
   {
      return (request.getParameter(requestParameterName) != null) ? request.getParameter(requestParameterName) : "";
   }

   @Override
   /**
    * Because we require the API client to send credentials with every request, we must authenticate on every request
    */
   protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
       return true;
   }
   
}
