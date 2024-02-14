package com.code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	public CustomAuthSucessHandler sucessHandler;
	
      @Bean	
      public BCryptPasswordEncoder passwordencoder() {
    	  
		return new BCryptPasswordEncoder();
      }	
      @Bean
      public UserDetailsService getDetailsService() {
		return new CustomUserDetailsService();
    }
      @Bean
      public DaoAuthenticationProvider getAuthenticationProvider() {
    	  DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
    	  authenticationProvider.setUserDetailsService(getDetailsService());
    	  authenticationProvider.setPasswordEncoder(passwordencoder());
    	  return authenticationProvider;
      }
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			/*
			 * http.csrf().disable().authorizeHttpRequests().requestMatchers("/","/register"
			 * ,"/signin","/saveUser").permitAll()
			 * .requestMatchers("/user/**").authenticated().and()
			 * .formLogin().loginPage("/signin").loginProcessingUrl("/userlogin") //
			 * .usernameParameter("email") .defaultSuccessUrl("/user/profile").permitAll();*/
    	  http.csrf().disable()
    	  .authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
    	  .requestMatchers("/admin/**").hasRole("ADMIN")
    	  .requestMatchers("/**").permitAll().and()
    	  .formLogin().loginPage("/signin").loginProcessingUrl("/userlogin")
    	  .successHandler(sucessHandler)
    	  .permitAll();
    	  http.authenticationProvider(getAuthenticationProvider());
			  return http.build();
			 
      }
}
