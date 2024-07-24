package it.pizzeria.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import it.pizzeria.service.DatabaseUserDetailsService;

@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http)
	throws Exception {
	     http.authorizeHttpRequests()
	                    .requestMatchers(HttpMethod.POST,"/home/pizze/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.POST,"/pizza/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/home/pizze/addpizza").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/home/pizze/editpizza/{id}").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/home/pizze/addoffer/{id}").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/pizza/**").hasAuthority("ADMIN")
	                    .requestMatchers("/home","/home/pizze").hasAnyAuthority("USER","ADMIN")
	                    .requestMatchers("/**").permitAll()
	                    .and().formLogin()
	                    .and().logout()
	                    .and().exceptionHandling()
	                    .and().csrf().disable();
	    return http.build();
	}
	
	
	@Bean
	DatabaseUserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
}
