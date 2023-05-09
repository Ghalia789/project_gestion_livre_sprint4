package com.nadhem.livres.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import
org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.nadhem.livres.entities.User;
import com.nadhem.livres.repos.UserRepository;

public class MyUserDetailsService  implements UserDetailsService{
	@Autowired
	UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws
	UsernameNotFoundException {
	User user = userRepository.findByUsername(username);
	return new MyUserDetails(user);
	 } 
}
