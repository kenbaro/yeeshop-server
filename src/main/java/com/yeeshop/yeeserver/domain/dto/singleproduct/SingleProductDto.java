package com.yeeshop.yeeserver.domain.dto.singleproduct;

import java.util.List;

import com.yeeshop.yeeserver.domain.entity.ProductAttribute;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;
import com.yeeshop.yeeserver.domain.entity.Sales;

import lombok.Data;

@Data
public class SingleProductDto {

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
	
	private ProductSeries productSeries;
	
	private List<RelatedProductDto> relatedProductDtos; 
	
	private SingleProductImage image;
	
	private Integer soldQty;
	
	private Integer stockQuantity;
	
	private String orderQty;
	
	private String tempPrice;
	
	private String otherProductLength;
	
	private Sales sales;
}
