package com.yeeshop.yeeserver.domain.dto.order;


import com.yeeshop.yeeserver.domain.entity.Sales;

import lombok.Data;

@Data
public class OrderDetailDto {

	private String oDetailsId;
	
	private String productNm;
	
	private String productImage;
	
	private String productColor;
	
	private String productStorage;
	
	private String orderQty;
	
	private String unitPrice;
	
	private String discount;
	
	private String voucher;
	
	private String newPrice;
	
	private Sales sales;
	
}
