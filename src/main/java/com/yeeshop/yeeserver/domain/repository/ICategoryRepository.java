package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, String>{

	List<Category> findByCateNm(String cateNm);
}
