package com.yeeshop.yeeserver.domain.service;

import java.util.List;
import java.util.Optional;

import com.yeeshop.yeeserver.domain.entity.User;

public interface IOAuth2UserService {

	List<User> getUsers();

    Optional<User> getUserByEmail(String email);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByEmail(String email);

    User saveUser(User user);

    void deleteUser(User user);
}
