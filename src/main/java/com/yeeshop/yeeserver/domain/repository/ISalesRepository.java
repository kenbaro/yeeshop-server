package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Sales;

@Repository
public interface ISalesRepository extends JpaRepository<Sales, String>{

	@Query("SELECT s FROM Sales s WHERE s.saleNm LIKE %:keyword%")
	List<Sales> findSalesContainingKeyWord(@Param("keyword") String kw);
	
	@Query("SELECT s FROM Sales s WHERE s.isExprired = 0 OR s.isExprired = 3")
	List<Sales> findSalesNotExpired();
}
