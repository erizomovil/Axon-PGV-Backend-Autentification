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

import com.axon.autentification.errorHandler.ApiError;
import com.axon.autentification.model.User;
import com.axon.autentification.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	String secretKey = "mySecretKey";
	
	@Autowired
	public UserRepository userRepository;
	
	@GetMapping("health")
	public ResponseEntity<String> healthCheck() {
	    return ResponseEntity.ok("Server is up");
	}
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String pwd) {
	    Optional<User> userOptional = userRepository.findByUsername(username);
	    
	    if (userOptional.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiError("Username not found", "username"));
	    }
	    
	    User userFromDb = userOptional.get();
	    boolean isMatch = passwordEncoder.matches(pwd, userFromDb.getHashedPassword());
	    
	    if (!isMatch) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiError("Incorrect password", "password"));
	    }

	    String token = getJWTToken(username);
	    userFromDb.setToken(token);
	    userRepository.save(userFromDb);

	    return ResponseEntity.ok().body("Login successful");
	}
		
	@PostMapping("register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String pwd) {
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiError("Username already exists", "username"));
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiError("Email already in use", "email"));
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setToken(getJWTToken(username));
	    user.setLevel(1);
	    user.setExperience(0);
            user.setHashedPassword(passwordHasher(pwd));
            userRepository.save(user);

            return ResponseEntity.ok().body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Error while registering the user", "unknown"));
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
