package com.yeeshop.yeeserver.domain.dto.singleproduct;

import lombok.Data;

@Data
public class RelatedProductDto {

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
