package com.yeeshop.yeeserver.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yeeshop.yeeserver.domain.entity.User;

public interface IUserRepository extends JpaRepository<User,String> {

	Boolean existsByEmail(String email);
    Optional<User> findByEmail(String userEmail);
    
}
