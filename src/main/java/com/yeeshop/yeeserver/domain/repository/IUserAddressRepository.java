package com.yeeshop.yeeserver.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.entity.UserAddress;

@Repository
public interface IUserAddressRepository extends JpaRepository<UserAddress, String>{

	public UserAddress findByUser(User user);
}
