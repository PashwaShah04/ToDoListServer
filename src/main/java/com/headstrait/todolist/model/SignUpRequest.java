package com.headstrait.todolist.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

	@NotBlank
	@Size(min = 3, max = 24)
	private String userName;
	@NotBlank
	@Size(min = 6, max = 24)
	private String password;
	private String fname;
	private String lname;
	@JsonProperty
	private boolean isAdmin;

}
