package com.yeeshop.yeeserver.app.form;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.cart.CartItem;

import lombok.Data;

@Data
public class CartForm {

	private List<CartItem> cartItems;
	
	private Integer cartCnt;
	
	private String tempPriceTotal;
}
