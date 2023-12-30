package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;

public interface IHomeRepository {

	public List<ProductSeries> getProductSeriesBySubCateId(String subCateId);
	
	public List<Product> getProductsByPSeriesIds (List<String> pramMap);
}
