package com.yeeshop.yeeserver.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yeeshop.yeeserver.domain.entity.Role;

public interface IRoleRepository extends JpaRepository<Role,String>{

	Role findByRoleName(String roleNm);
}
