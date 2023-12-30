package com.yeeshop.yeeserver.domain.dto.cart;

import java.util.List;

import lombok.Data;

@Data
public class CartDto {

	private List<CartItem> cartItems;
	
	private Integer cartCnt;
	
	private String tempPriceTotal;
}
