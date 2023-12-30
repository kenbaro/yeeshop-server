package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import jakarta.servlet.http.HttpServletRequest;

public interface IBrandService {

	public List<Brand> getAllBrands();
	
	public Brand getBrandById(HttpServletRequest request);
	
	public Boolean updateBrand(Brand brand, YeeMessage yeeMessage);
	
	public Boolean addBrand(Brand brand, YeeMessage yeeMessage);
	
	public Boolean deleteBrand(HttpServletRequest request, YeeMessage yeeMessage);
}
