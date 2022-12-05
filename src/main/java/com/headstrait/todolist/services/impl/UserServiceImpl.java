package com.headstrait.todolist.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.repository.UserRepository;
import com.headstrait.todolist.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUserByName(String username) {
		return userRepository.getByUserName(username).get();
	}

	@Override
	public boolean checkUser(String userName) {
		return userRepository.existsByUserName(userName);
	}

	@Override
	public void createUser(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public User updateUser(long userId, String username, String fname, String lname, String password) {
		userRepository.updateUser(userId, username, fname, lname, password);
		return getUserById(userId);
	}

	@Override
	public User getUserById(long userId) {
		return userRepository.getByUserId(userId).get();
	}

}
