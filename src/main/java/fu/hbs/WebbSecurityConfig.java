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

package fu.hbs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fu.hbs.service.dao.NewsService;
import fu.hbs.service.dao.ReceptionistBookingService;
import fu.hbs.service.dao.ServiceService;
import fu.hbs.service.impl.CustomizeUserDetailsService;
import fu.hbs.service.impl.NewsServiceImpl;
import fu.hbs.service.impl.ReceptionistBookingServiceImpl;
import fu.hbs.service.impl.ServiceServiceImpl;

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

    @Bean
    public ServiceService serviceService() {
        return new ServiceServiceImpl();
    }

    @Bean
    public NewsService newsService() {
        return new NewsServiceImpl();
    }

    @Bean
    public ReceptionistBookingService receptionistBookingService() {
        return new ReceptionistBookingServiceImpl();
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

        http.logout(auth -> auth.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID", "remember-me").permitAll());

        http.rememberMe(auth -> auth.key("$2a$12$AcViNz3G9VDHfEpIudFr/.kRoiR.blVJXzGzlcgQwp608WnQyuA7C")
                .tokenValiditySeconds(604800));

        // Authorization

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/assets/**", "/", "/feedback/homepage", "/feedback/save", "/homepage", "/login", "/registration", "/services", "/service-details", "/news", "/news-details", "/hbs/**", "room/**",
                        "/error", "/submitOrder")
                .permitAll().requestMatchers("/admin/**").hasAuthority("ADMIN").requestMatchers("/customer/**")
                .hasAuthority("CUSTOMER").requestMatchers("/management/**").hasAuthority("MANAGEMENT")
                .requestMatchers("/receptionist/**").hasAuthority("RECEPTIONISTS").requestMatchers("/housekeeping/**")
                .hasAuthority("HOUSEKEEPING").requestMatchers("/accounting/**").hasAuthority("ACCOUNTING").anyRequest()
                .authenticated());

        // Exception Handling
        http.exceptionHandling(auth -> auth.accessDeniedPage("/accessDenied"));

        //

        return http.build();
    }
}
