package com.headstrait.todolist.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
	@NotBlank
	private long userId;
	@NotBlank
	private String user;
	@NotBlank
	private String password;
	private String fname;
	private String lname;
	private String username;
	@Size(min = 6, max = 24)
	private String newPassword;

}
