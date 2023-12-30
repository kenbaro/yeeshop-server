package com.yeeshop.yeeserver.app.form;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.common.ProductCartDto;
import com.yeeshop.yeeserver.domain.dto.ourstore.StoreBrandDto;

import lombok.Data;

@Data
public class StoreForm {

	private List<ProductCartDto> products;
	
	private List<StoreBrandDto> brands;
	
	private Integer productShowQty;
	
	private Integer productRemainingQty;
	
	private String filterBrand;
	
	private String filterFromPrice;
	
	private String filterToPrice;
	
	private String filterBy;
	
	private Boolean showMore = false;
}
