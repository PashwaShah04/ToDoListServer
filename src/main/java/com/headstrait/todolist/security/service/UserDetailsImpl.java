package com.headstrait.todolist.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.headstrait.todolist.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1006735248221932731L;
	private Long id;
	private String username;
	private String password;
	private String fname;
	private String lname;
	private boolean isAdmin;
	private Collection<? extends GrantedAuthority> authorities;

	public static UserDetails build(User user) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (user.isAdmin()) {
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		}

		return new UserDetailsImpl(user.getUserId(), user.getUserName(), user.getPassword(), user.getFname(),
				user.getLname(), user.isAdmin(), authorities);

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
