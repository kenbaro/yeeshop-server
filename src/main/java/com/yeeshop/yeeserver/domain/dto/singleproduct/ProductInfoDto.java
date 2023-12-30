package com.yeeshop.yeeserver.domain.dto.singleproduct;

import java.util.List;

import com.yeeshop.yeeserver.domain.entity.ProductAttribute;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;
import com.yeeshop.yeeserver.domain.entity.Sales;

import lombok.Data;

@Data
public class ProductInfoDto {
	
	private String SKU;
	
	private String productNm;
	
	private String productCate;
	
	private String productCateNm;
	
	private String productBrandCd;
	
	private String productBrandNm;
	
	private Integer productStatus;
	
	private List<SingleProductColor> productColors;
	
	private SingleProductColor productColor;
	
	private List<SingleProductStorageDto> productStorages;
	
	private SingleProductStorageDto productStorageDto;

	private String unitPrice;
	
	private String productDesription;
	
	private ProductAttribute productAttribute;
	
	private ProductSeries productSeries;
	
	private SingleProductImage image;
	
	private String color;
	private Integer stockQuantity;
	
	private Integer soldQty;

	private String otherProductLength;
	
	private String saleTitle;
	private Sales sales;

}
