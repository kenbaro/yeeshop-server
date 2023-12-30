package com.yeeshop.yeeserver.domain.dto.cart;

import com.yeeshop.yeeserver.domain.entity.Sales;

import lombok.Data;

@Data
public class CartItem {

	private String cartId;

	private String SKU;
	
	private String productNm;
	
	private String productColor;

	private String image;
	
	private String orderQty;
	
	private String unitPrice;
	
	private String newPrice;
	
	private String tempPrice;

	private String discount;
	
	private Sales sales;
	
	private String priceVoucher;
	
	private String voucherNm;
	
	private Boolean isAvailable;
	
	private Integer productQty;
}
