package com.yeeshop.yeeserver.domain.dto.ourstore;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.common.ProductCartDto;

import lombok.Data;

@Data
public class StoreDto {

	private List<ProductCartDto> products;
	
	private List<StoreBrandDto> brands;
	
	private Integer productShowQty = 0;
	
	private Integer productRemainingQty = 0;
	
	private String filterBrand;
	
	private String filterFromPrice;
	
	private String filterToPrice;
	
	private String filterBy;
	
	private Boolean showMore = false;

}
