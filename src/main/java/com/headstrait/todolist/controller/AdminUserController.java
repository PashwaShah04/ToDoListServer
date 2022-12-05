package com.headstrait.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.services.UserService;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public List<User> getUsers() {
		return userService.getUsers();
	}
}
