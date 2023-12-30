package com.yeeshop.yeeserver.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.exeption.UserNotFoundException;
import com.yeeshop.yeeserver.domain.repository.IUserRepository;
import com.yeeshop.yeeserver.domain.service.IOAuth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements IOAuth2UserService{

	 	private final IUserRepository userRepository;

	    @Override
	    public List<User> getUsers() {
	        return userRepository.findAll();
	    }

	    @Override
	    public boolean hasUserWithEmail(String email) {
	        return userRepository.existsByEmail(email);
	    }

	    @Override
	    public User validateAndGetUserByEmail(String email) {
	        return getUserByEmail(email)
	                .orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", email)));
	    }

	    @Override
	    public User saveUser(User user) {
	        return userRepository.save(user);
	    }

	    @Override
	    public void deleteUser(User user) {
	        userRepository.delete(user);
	    }

		@Override
		public Optional<User> getUserByEmail(String email) {
			return userRepository.findByEmail(email);
		}

}
