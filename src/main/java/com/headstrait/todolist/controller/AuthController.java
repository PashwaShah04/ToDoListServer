package com.headstrait.todolist.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.JwtAuthResponse;
import com.headstrait.todolist.model.LoginRequest;
import com.headstrait.todolist.model.MessageResponse;
import com.headstrait.todolist.model.SignUpRequest;
import com.headstrait.todolist.security.service.UserDetailsImpl;
import com.headstrait.todolist.security.service.jwt.JwtUtils;
import com.headstrait.todolist.services.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

		if (userService.checkUser(signUpRequest.getUserName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Username is taken"));
		}
		String fname = signUpRequest.getFname().toUpperCase().charAt(0)
				+ signUpRequest.getFname().toLowerCase().substring(1);
		String lname = signUpRequest.getLname().toUpperCase().charAt(0)
				+ signUpRequest.getLname().toLowerCase().substring(1);

		User user = User.builder().userName(signUpRequest.getUserName())
				.password(passwordEncoder.encode(signUpRequest.getPassword())).fname(fname).lname(lname)
				.isAdmin(signUpRequest.isAdmin()).build();

		userService.createUser(user);
		return ResponseEntity.ok(new MessageResponse("User Registered"));
	}

	@GetMapping("/signup/validate/{name}")
	public ResponseEntity<?> validateUser(@PathVariable("name") String name) {
		if (userService.checkUser(name)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Username is taken"));
		}
		return ResponseEntity.ok(new MessageResponse(""));
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody UpdateRequest updateRequest) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				updateRequest.getUser(), updateRequest.getPassword());
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials");
		}
		String password;
		if (updateRequest.getNewPassword() == "") {
			password = authenticationToken.getCredentials().toString();
		} else {
			password = updateRequest.getNewPassword();
		}

		User updatedUser = userService.updateUser(updateRequest.getUserId(), updateRequest.getUsername(),
				updateRequest.getFname(), updateRequest.getLname(), passwordEncoder.encode(password));
		authenticationToken = new UsernamePasswordAuthenticationToken(updatedUser.getUserName(), password);
		try {
			authentication = authenticationManager.authenticate(authenticationToken);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something went Wrong");
		}
		String token = jwtUtils.generateJWT(authentication);
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(token, (UserDetailsImpl) authentication.getPrincipal());
		return ResponseEntity.ok(jwtAuthResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials");
		}
		String token = jwtUtils.generateJWT(authentication);
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(token, (UserDetailsImpl) authentication.getPrincipal());
		return ResponseEntity.ok(jwtAuthResponse);
	}
}
