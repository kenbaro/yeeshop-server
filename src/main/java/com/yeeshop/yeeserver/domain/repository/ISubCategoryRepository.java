package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.SubCategory;

@Repository
public interface ISubCategoryRepository extends JpaRepository<SubCategory, String>{

	@Query("SELECT sb FROM SubCategory sb WHERE sb.category.cateCd = ?1")
	List<SubCategory> findByCategoryId(String id);
	
	@Query("SELECT sb FROM SubCategory sb WHERE sb.subCateNm=?1 AND sb.category.cateNm = ?2")
	List<SubCategory> findBySubCateNmAndCateNm(String subCateNm, String cateNm);
}
