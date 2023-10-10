/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 * 
 * 
 */
package com.example.services.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entities.Role;
import com.example.repositoties.RoleRepository;
import com.example.repositoties.UserRepository;

@Service("customizeUserDetailsService")
public class CustomizeUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;

	public CustomizeUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		com.example.entities.User result = userRepository.findByEmail(email);
		System.out.println(email);

		if (result != null) {

			List<Role> roles = roleRepository.findRole(result.getUserId());

			List<GrantedAuthority> authorities = new ArrayList<>();

			GrantedAuthority authority = null;

			for (Role role : roles) {
				authority = new SimpleGrantedAuthority(role.getRoleName());

				System.out.println(role.getRoleName());

				authorities.add(authority);
			}

			User user = new org.springframework.security.core.userdetails.User(result.getEmail(), result.getPassword(),
					true, true, true, true, authorities);
			return (UserDetails) user;
		} else {
			throw new UsernameNotFoundException("Email or Password is invalid!");
		}
	}

}
