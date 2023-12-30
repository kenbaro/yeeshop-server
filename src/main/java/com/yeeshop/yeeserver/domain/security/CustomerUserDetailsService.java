package com.yeeshop.yeeserver.domain.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService{
	
	private final IUserRepository iUserRepository ;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = iUserRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found !"));
        return user ;

    }
	
}
