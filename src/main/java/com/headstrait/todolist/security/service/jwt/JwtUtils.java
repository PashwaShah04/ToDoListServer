package com.headstrait.todolist.security.service.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.headstrait.todolist.security.service.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateJWT(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		Map<String, Object> claims = new HashMap<>();

		claims.put("id", userPrincipal.getId());
		claims.put("name", userPrincipal.getFname());
		claims.put("name", userPrincipal.getLname());
		claims.put("username", userPrincipal.getUsername());
		claims.put("isAdmin", userPrincipal.isAdmin());

		return Jwts.builder().addClaims(claims).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

	}

	public String getUserNameFromJWT(String token) {
		return getAllClaimsFromToken(token).get("username").toString();
	}

	public boolean validateJWT(String token, UserDetails userDetails) {
		try {
			final String username = getUserNameFromJWT(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		} catch (SignatureException e) {
			System.err.println("Invalid JWT Signature: " + e.getMessage());
		} catch (MalformedJwtException e) {
			System.err.println("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.err.println("Expired JWT token: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.err.println("Unsupported JWT token: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println("JWT claims string is empty: " + e.getMessage());

		}
		return false;
	}

}
