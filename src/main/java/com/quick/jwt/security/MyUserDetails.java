package com.quick.jwt.security;

import com.quick.jwt.model.Role;
import com.quick.jwt.model.User;
import com.quick.jwt.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

	@Value("${security.jwt.token.universal}")
	private String universalToken;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUsername(username);
		if (user == null && !universalToken.equals(username)) {
			throw new UsernameNotFoundException("User '" + username + "' not found");
		}
		List<Role> roles;
		if(user != null) {
			roles = user.getRoles();
		}else {
			roles = new ArrayList<Role>();
			roles.add(Role.ROLE_ADMIN);
		}
		return org.springframework.security.core.userdetails.User//
				.withUsername(username)//
				.password(user!=null?user.getPassword():universalToken)//
				.authorities(roles)//
				.accountExpired(false)//
				.accountLocked(false)//
				.credentialsExpired(false)//
				.disabled(false)//
				.build();
	}

}