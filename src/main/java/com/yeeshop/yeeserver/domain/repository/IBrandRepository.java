package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yeeshop.yeeserver.domain.entity.Brand;

public interface IBrandRepository extends JpaRepository<Brand, String>{

	List<Brand> findByBrandNm(String brandNm);
}
