package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.productgroup.ProductGroupDto;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import jakarta.servlet.http.HttpServletRequest;

public interface IProductGroupService {

	public List<ProductGroupDto> getAllProductGroup();
	
	public void getAllMainCategory(HttpServletRequest request,ProductGroupDto productGroupDto);
	
	public void getAllSubCategory(HttpServletRequest request,ProductGroupDto productGroupDto);
	
	public void getProductGroupById(HttpServletRequest request,ProductGroupDto productGroupDto);
	
	public Boolean addCate(HttpServletRequest request,ProductGroupDto productGroupDto, YeeMessage msg);
	
	public Boolean updateCate(ProductGroupDto productGroupDto, YeeMessage msg);
	
	public Boolean deleteCate(HttpServletRequest request, YeeMessage msg);
}
