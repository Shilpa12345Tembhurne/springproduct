package com.springboot.first.demo.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.first.demo.user.User;

@Component
public class JwtTokenFilter  extends OncePerRequestFilter
{
	@Autowired
	private JwtTokenUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		
		
		if(!hasAuthorizationBearer(request))
		{
			filterChain.doFilter(request, response);
			return ;
		}
		
		   String accesTtoken = getAccessToken(request);
		   if(!jwtUtil.validateAccessToken(accesTtoken))
			  {
				  filterChain.doFilter(request, response);
				  return;
			  }
		   
		   setAuthenticationContext(accesTtoken, request);
		      filterChain.doFilter(request, response);
		   
		 
	 
	  
	 
	  
	}
	private void setAuthenticationContext(String accesToken, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(accesToken);
 
        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
 
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
	 private UserDetails getUserDetails(String accesToken)  {
	        User userDetails = new User();
	        String[] Subjectarray = jwtUtil.getSubject(accesToken).split(",");
	 
	        userDetails.setId(Integer.parseInt(Subjectarray[0]));
	        userDetails.setEmail(Subjectarray[1]);
	 
	        return userDetails;
	    }
		
	private boolean hasAuthorizationBearer(HttpServletRequest request) 
	{
		String header=request.getHeader("Authorization");
		System.out.println("Authorization header:"+header);
		
       
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
 
        return true;
    }
 
    private String getAccessToken(HttpServletRequest request) {
    	
        String header = request.getHeader("Authorization");
        
        String token = header.split(" ")[1].trim();
        
        System.out.println("Access Token: "+token);
        return token;
    }
 
    
 
   
	
	

}
