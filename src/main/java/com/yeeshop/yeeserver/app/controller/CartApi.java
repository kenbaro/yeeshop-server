package com.yeeshop.yeeserver.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.CartForm;
import com.yeeshop.yeeserver.app.form.common.YeeMessageForm;
import com.yeeshop.yeeserver.domain.dto.cart.CartDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.service.ICartService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@NoArgsConstructor
@AllArgsConstructor
public class CartApi {

	@Autowired ModelMapper mapper;

	@Autowired
	ICartService cartService;
	
	@GetMapping("/get-cart")
	public ResponseEntity<?> index() {
		
		CartDto cartDto = new CartDto();
		
		this.cartService.initCart(cartDto);
		
		CartForm cartForm = this.mapper.map(cartDto, CartForm.class);
		
		return new ResponseEntity<>(cartForm,HttpStatus.OK);
	}
	
	@PostMapping("/add-to-cart")
	public ResponseEntity<?> addToCart(@RequestBody CartForm cartForm){
		
		CartDto cartDto = this.mapper.map(cartForm, CartDto.class);
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();

		yeeMessageDto = this.cartService.insertCart(cartDto, yeeMessageDto);
		
		YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);
		
		return new ResponseEntity<>(yeeMessageForm, HttpStatus.OK);
	}
	
	@GetMapping("/delete-cart-api-v1/id={cartId}")
	public ResponseEntity<?> deleteCartApiv1(@PathVariable String cartId) {
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		yeeMessageDto = this.cartService.deleteCart(cartId, yeeMessageDto);

		YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);
		return new ResponseEntity<>(yeeMessageForm, HttpStatus.OK);
	}
	
	@GetMapping("/update-cart-api-v1/cartId={cartId}/qty={qty}")
	public ResponseEntity<?> updateCartApiv1 (@PathVariable String cartId, @PathVariable String qty) {
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		yeeMessageDto = this.cartService.updateCartItem(cartId, qty, yeeMessageDto);
		YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);
		return new ResponseEntity<>(yeeMessageForm,HttpStatus.OK);
	}
	
	@PostMapping("/validate-before-switch-to-pay-v1")
	public ResponseEntity<?> validateBeforeSwitchToPay(@RequestBody CartForm cartForm) {
		
		CartDto cartDto = this.mapper.map(cartForm, CartDto.class);
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();

		yeeMessageDto = this.cartService.validateBeforeSwitchToPay(cartDto.getCartItems(), yeeMessageDto);
		
		YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);
		
		return new ResponseEntity<>(yeeMessageForm, HttpStatus.OK);
	}
	
}
