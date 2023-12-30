package com.yeeshop.yeeserver.domain.dto.common;

import lombok.Data;

@Data
public class ProductCartDto {

	private String productCd;
	
	private String productNm;
	
	private String productImg;
	
	private String productUnitPrice;
	
	private String productDiscountPrice;
	
	private String productDescription;
	
	private Integer productDiscount;
	
	private String brandCd;
	
	private String brandNm;
}
