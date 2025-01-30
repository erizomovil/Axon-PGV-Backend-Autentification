package com.axon.autentification.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.axon.autentification.model.User;
import com.axon.autentification.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	String secretKey = "mySecretKey";
	
	@Autowired
	public UserRepository userRepository;
	

	@PostMapping("login")
	public void login(@RequestParam("username") String username, @RequestParam("password") String pwd) throws Exception {
	    Optional<User> userOptional = userRepository.findByUsername(username);
	    if (userOptional.isEmpty()) {
	        throw new Exception("Invalid username or password");
	    }
	    User userFromDb = userOptional.get();
	    boolean isMatch = passwordEncoder.matches(pwd, userFromDb.getHashedPassword());
	    if (isMatch) {
	        String token = getJWTToken(username);
	        userFromDb.setToken(token);
	        userRepository.save(userFromDb);
	    } else {
	        throw new Exception("Invalid password");
	    }
	}
	
	@PostMapping("register")
	public void register(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String pwd) throws Exception {
		try {
		    User user = new User();
		    user.setUsername(username);
		    user.setEmail(email);
		    user.setToken(getJWTToken(username));
		    user.setHashedPassword(passwordHasher(pwd));
		    userRepository.save(user);
		} catch (Exception e) {
		    throw new Exception("Error while registering the user: " + e.getMessage(), e);
		}
	}
	

	private String getJWTToken(String username) {

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
	
	private String passwordHasher(String pwd) {
		
		String hashedPassword = passwordEncoder.encode(pwd);
		return hashedPassword;
	}
}
