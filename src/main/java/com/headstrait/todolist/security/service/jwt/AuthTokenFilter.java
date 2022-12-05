package com.headstrait.todolist.security.service.jwt;

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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.headstrait.todolist.security.service.UserDetailsServiceImpl;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseAuthHeaderAndGetJWT(request);
			if (jwt != null) {
				String username = jwtUtils.getUserNameFromJWT(jwt);
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
				if (jwtUtils.validateJWT(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					authentication.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

		} catch (Exception e) {
			System.err.println("Cannot set user authentication: " + e.getMessage());
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);

	}

	private String parseAuthHeaderAndGetJWT(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7, authHeader.length());
		}
		return null;

	}

}
