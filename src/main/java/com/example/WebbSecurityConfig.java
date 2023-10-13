/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 *  * 
 */

package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.services.dao.impl.CustomizeUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebbSecurityConfig {
	private CustomizeUserDetailsService customizeUserDetailsService;

	public WebbSecurityConfig(CustomizeUserDetailsService customizeUserDetailsService) {
		super();
		this.customizeUserDetailsService = customizeUserDetailsService;
	}

	// mã hoá
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void configureGlobal(AuthenticationManagerBuilder managerBuilder) throws Exception {
		System.out.println("Authentication manager!");
		managerBuilder.userDetailsService(customizeUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// Disable csrf
		http.csrf(csrf -> csrf.disable());

		// Authentication

		// Cấu hình cho Login Form.
		http.formLogin(auth -> auth.loginPage("/login").usernameParameter("email").loginProcessingUrl("/loginProcess")
				.defaultSuccessUrl("/homepage").failureUrl("/login?error"));
		http.logout(auth -> auth.logoutUrl("/logout").logoutSuccessUrl("/login?logout"));

		// Authorization

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/assets/**", "/", "/homepage", "/login", "/registration")
				.permitAll().requestMatchers("/admin/**").hasAnyRole("Admin").requestMatchers("/user/**")
				.hasAnyRole("Customer").requestMatchers("/management/**").hasAnyRole("Management")
				.requestMatchers("/receptionist/**").hasAnyRole("Receptionists").requestMatchers("/housekeeping/**")
				.hasAnyRole("Housekeeping").requestMatchers("/accounting/**").hasAnyRole("Accounting").anyRequest()
				.authenticated());

		// Exception Handling
		http.exceptionHandling(auth -> auth.accessDeniedPage("/accessDenied"));

		//

		return http.build();
	}
}
