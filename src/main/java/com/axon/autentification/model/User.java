package com.axon.autentification.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
    private Long id;
    
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "level")
    private Integer level;
    
    @Column(name = "experience")
    private Integer experience;
    
    @Column(name = "registration_date")
    private LocalDateTime registration_date;
    
    @Column(name = "hashedPassword")
    private String hashedPassword;
    
    @Column(name = "token")
    private String token;
    
    public User() {
    	this.registration_date = LocalDateTime.now();
    }

	public User(Long id, String username, String email, Integer level, Integer experience, LocalDateTime registration_date,
			String hashedPassword, String token) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.level = level;
		this.experience = experience;
		this.registration_date = registration_date;
		this.hashedPassword = hashedPassword;
		this.token = token;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public LocalDateTime getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(LocalDateTime registration_date) {
		this.registration_date = registration_date;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
