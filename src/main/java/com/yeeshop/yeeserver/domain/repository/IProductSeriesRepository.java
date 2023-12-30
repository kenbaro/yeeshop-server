package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.ProductSeries;

@Repository
public interface IProductSeriesRepository extends JpaRepository<ProductSeries, String>{

	List<ProductSeries> findBypSeriesNmContaining(String infix);
	
	@Query("SELECT ps FROM ProductSeries ps WHERE ps.subCategory.subCateCd = ?1")
	List<ProductSeries> findProductSeriesBySubCateCd(String id);
	
	@Query("SELECT ps FROM ProductSeries ps WHERE ps.pSeriesNm = ?1")
	List<ProductSeries> findByPSeriesNm(String pSeriesNm);
}
