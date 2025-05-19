package com.ssafy.pjt.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.pjt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ssafy.pjt.user.service.CustomUserDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private final JwtUtil jwtUtil;
	private final CustomUserDetailService customUserDetailService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String token = authorizationHeader.replace("Bearer ", "");
	        String userId = jwtUtil.extractUserId(token);
	        
	        log.debug("userId :{}", userId);
	        
	        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        	UserDetails userDetails = customUserDetailService.loadUserByUserId(userId);
	        	
	        	if(jwtUtil.validateToken(token, userDetails)) {
	        		
	        		log.debug("userDetails : {}", userDetails.toString());
	        		
	        		UsernamePasswordAuthenticationToken authentication =
	        				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        		SecurityContextHolder.getContext().setAuthentication(authentication);
	        	}
	        }
		}
		filterChain.doFilter(request, response);
	}
}
