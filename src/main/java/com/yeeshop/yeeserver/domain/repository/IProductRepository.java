package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.Sales;

@Repository
public interface IProductRepository extends JpaRepository<Product, String>{

	@Query("SELECT p FROM Product p WHERE p.SKU = ?1 AND  p.isAvailabled = 1")
	Product findActiveProductBySKU(String SKU);
	
	@Query("SELECT p FROM Product p WHERE p.isAvailabled = 1")
	List<Product> findAvailableProducts();
	
	@Query("SELECT p FROM Product p WHERE p.isAvailabled = 1 AND p.productSeries.pSeriesCd = ?1")
	List<Product> findAvailableProductsBySeries(String id);

	@Query("SELECT p FROM Product p WHERE p.productSeries.pSeriesCd = ?1")
	List<Product> findProductsBySeries(String id);
	
	@Query("SELECT p FROM Product p WHERE p.productSeries.pSeriesCd <> ?1")
	List<Product> findProductsExceptSeries(String id);
	
	List<Product> findTop8ByIsAvailabled(Integer isAvailabled);
	
	List<Product> findByBrand(Brand brand);
	
	List<Product> findByProductNmContaining(String infix);
	
	List<Product> findByProductNm(String productNm);
	
	List<Product> findBySale(Sales sale);
	
	@Query("SELECT p.productNm FROM Product p GROUP BY p.productNm")
	List<String> findAllProductNm();
	
}
