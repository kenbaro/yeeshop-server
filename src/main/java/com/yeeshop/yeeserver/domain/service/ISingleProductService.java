package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.admin.dto.productmanage.EditProductDto;
import com.yeeshop.yeeserver.domain.admin.dto.productmanage.SubEditProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.AutoCompleteProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.ProductInfoDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductDto;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for Home Service.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
public interface ISingleProductService {

	public void initProduct(SingleProductDto singleProductDto, String SKU) throws Exception;
	
	public void getProduct(SingleProductDto singleProductDto, String storageCd, String colorCd) throws Exception;
	
	public void getRelatedProduct(SingleProductDto singleProductDto);
	
	public Boolean validateProduct(SingleProductDto singleProductDto);
	
	public Boolean validateKeyWord(String kw);
	
	public void initAutoComplete(AutoCompleteProductDto autoCompleteProductDto, String kw);
	
	public List<ProductInfoDto> getAllProduct(HttpServletRequest request);
	
	public Boolean editActiveProductByAdmin(String sku, YeeMessage message, Boolean isActive);
	
	public void initProductDto(EditProductDto productDto);
	
	public Boolean updateProduct(EditProductDto productDto, YeeMessage yeeMessage);
	
	public Boolean deleteProducts(List<String> idItems, YeeMessage yeeMessage);
	
	public void initProductDtoNew(EditProductDto productDto);
	
	public List<SubEditProductDto> getProductsByNm(HttpServletRequest request);
}
