package com.axon.autentification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(scanBasePackages = "com.axon")
@RestController
public class PruebaTokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaTokenApplication.class, args);
	}
	
	@Configuration
	@EnableWebSecurity
	public class SecurityConfig {
		@Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	            .authorizeHttpRequests(authz -> authz
	                .requestMatchers("/login").permitAll()
	                .requestMatchers("/register").permitAll()
	                .requestMatchers("/health").permitAll()
	                .anyRequest().authenticated()
	            )
	            .formLogin().disable() 
	            .httpBasic().disable(); 
	        return http.build();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
		
	}
	
}


