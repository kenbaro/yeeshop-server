package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.cart.CartDto;
import com.yeeshop.yeeserver.domain.dto.cart.CartItem;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.Cart;

public interface ICartService {

	public void initCart(CartDto cartDto);
	
	public YeeMessageDto insertCart(CartDto cartDto, YeeMessageDto yeeMessageDto);
	
	public Cart updateCart(CartDto cartDto, YeeMessageDto yeeMessageDto);
	
	public YeeMessageDto deleteCart(String cartId, YeeMessageDto yeeMessageDto);
	
	public YeeMessageDto updateCartItem(String cartId, String qty, YeeMessageDto yeeMessageDto);
	
	public YeeMessageDto validateBeforeSwitchToPay(List<CartItem> cartItems, YeeMessageDto yeeMessageDto);
}
