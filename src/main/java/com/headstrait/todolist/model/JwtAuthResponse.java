package com.headstrait.todolist.model;

import com.headstrait.todolist.security.service.UserDetailsImpl;

import lombok.Data;

@Data
public class JwtAuthResponse {

	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String fname;
	private String lname;
	private boolean isAdmin;

	public JwtAuthResponse(String token, Long id, String username, String fname, String lname, boolean isAdmin) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.isAdmin = isAdmin;
	}

	public JwtAuthResponse(String token, UserDetailsImpl userDetailsImpl) {
		this(token, userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getFname(),
				userDetailsImpl.getLname(), userDetailsImpl.isAdmin());
	}

}
