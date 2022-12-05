package com.headstrait.todolist.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with Username: " + username));
		return UserDetailsImpl.build(user);

	}

}
