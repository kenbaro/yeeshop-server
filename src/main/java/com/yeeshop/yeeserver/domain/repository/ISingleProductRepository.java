package com.yeeshop.yeeserver.domain.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Product;

@Repository
public interface ISingleProductRepository {

	public Product getProductByStorageAndColor(HashMap<String, Object> paramMap);
	
	public List<Product> getProductBySeriesId(HashMap<String, Object> paramMap);
	
	public List<Product> getProductBySeriesIdAndStorage(HashMap<String, Object> paramMap);
}
