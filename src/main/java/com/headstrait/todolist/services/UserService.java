package com.headstrait.todolist.services;

import java.util.List;

import com.headstrait.todolist.entity.User;

public interface UserService {

	User getUserByName(String username);

	boolean checkUser(String userName);

	void createUser(User user);

	List<User> getUsers();

	User updateUser(long userId, String username, String fname, String lname, String password);

	User getUserById(long userId);

}
