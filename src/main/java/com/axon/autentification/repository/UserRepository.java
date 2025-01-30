package com.axon.autentification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axon.autentification.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}