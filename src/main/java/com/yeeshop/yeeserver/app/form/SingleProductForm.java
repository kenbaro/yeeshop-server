package com.yeeshop.yeeserver.app.form;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.singleproduct.RelatedProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductColor;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductImage;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductStorageDto;
import com.yeeshop.yeeserver.domain.entity.ProductAttribute;
import com.yeeshop.yeeserver.domain.entity.Sales;

import lombok.Data;

/**
 * Form Object for SingleProduct Api.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
public class SingleProductForm {

private String SKU;
	
	private String productNm;
	
	private String productCate;
	
	private String productCateNm;
	
	private String productBrand;
	
	private String productBrandNm;
	
	private Integer productStatus;
	
	private List<SingleProductColor> productColors;
	
	private SingleProductColor productColor;
	
	private List<SingleProductStorageDto> productStorages;
	
	private SingleProductStorageDto productStorageDto;
	
	private String newPrice;
	
	private String oldPrice;
	
	private String productDesription;
	
	private ProductAttribute productAttribute;
	
	private List<RelatedProductDto> relatedProductDtos;

	private SingleProductImage image;
	
	private Integer soldQty;
	
	private String orderQty;
	
	private String tempPrice;
	private Sales sales;
}
