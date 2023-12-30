package com.yeeshop.yeeserver.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.yeeshop.yeeserver.app.form.CheckoutForm;
import com.yeeshop.yeeserver.app.form.common.YeeMessageForm;
import com.yeeshop.yeeserver.domain.dto.checkout.CheckoutDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.order.OrderInfoDto;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.ICartService;
import com.yeeshop.yeeserver.domain.service.ICheckoutService;
import com.yeeshop.yeeserver.domain.service.IOrderService;
import com.yeeshop.yeeserver.domain.service.impl.VnPayService;
import com.yeeshop.yeeserver.domain.utils.YeeSendMailUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class CheckoutApi {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired 
	private ICheckoutService checkoutSerivce;
	
	@Autowired 
	private ICartService cartService;
	
	@Autowired 
	private VnPayService vnPayService;

	@Autowired
	private IOrderService orderService;

	@PostMapping("/checkout-api-v1")
	public ResponseEntity<?> getCheckOutApiv1(@RequestBody CheckoutForm checkoutForm) throws JsonParseException, IOException {
		
		CheckoutDto checkoutDto = this.mapper.map(checkoutForm, CheckoutDto.class);
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		yeeMessageDto = this.cartService.validateBeforeSwitchToPay(checkoutDto.getCartItems(), yeeMessageDto);
		
		if (yeeMessageDto.getIsError()) {
			
			YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);

			return new ResponseEntity<>(yeeMessageForm, HttpStatus.OK);
		}

		this.checkoutSerivce.getUserInfo(checkoutDto);

		this.checkoutSerivce.getDeliveryUnitInfo(checkoutDto);

		if (YeeStringUtils.isEmpty(checkoutDto.getProvinceCd())) {
			
			this.checkoutSerivce.getProvinceInfo(checkoutDto);
		}
		
		this.checkoutSerivce.getDistrictInfo(checkoutDto);
		
		this.checkoutSerivce.getWardInfo(checkoutDto);
		
		this.checkoutSerivce.initItem(checkoutDto);
		
		this.checkoutSerivce.getPaymentMethod(checkoutDto);

		this.checkoutSerivce.getDeliveryService(checkoutDto);
		
		this.checkoutSerivce.calcFeeDelivery(checkoutDto);
		
		this.checkoutSerivce.calcTotalPrice(checkoutDto);
		
		checkoutForm = new CheckoutForm();
		this.mapper.map(checkoutDto, checkoutForm);
		
		return new ResponseEntity<>(checkoutForm, HttpStatus.OK);
	}

	@PostMapping("/create-payment-url")
	public ResponseEntity<?> createPaymentUrl(@RequestBody CheckoutForm checkoutForm) throws IOException {

		CheckoutDto checkoutDto = this.mapper.map(checkoutForm, CheckoutDto.class);
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		String url= this.checkoutSerivce.createPayment(checkoutDto, yeeMessageDto);
		
		if (YeeStringUtils.isNotEmpty(url) && !yeeMessageDto.getIsError()) {
			
			return new ResponseEntity<>(url, HttpStatus.OK);
		} 
		
		YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);
		
		return new ResponseEntity<>(yeeMessageForm, HttpStatus.INTERNAL_SERVER_ERROR);
		
    }
	
	@GetMapping("/vn-pay-payment/orderId={orderId}")
    public String getOrderReturn(HttpServletRequest request, @PathVariable String orderId) throws IOException{
		
		try {
			
			Integer paymentStatus = this.vnPayService.orderReturn(request);
			
	        if (paymentStatus == 1) {
	        	
	        	this.checkoutSerivce.updatePaymentCallBack(orderId, request, true);

	        	return "/order-return/"+orderId;
	        	
	        } else {
	        	
	        	this.checkoutSerivce.updatePaymentCallBack(orderId, request, false);
	        	return"/checkout/:id";
	        }
		} catch (Exception e) {
			
			return "";
		}
        
    }
	
	
	@GetMapping("/get-order-info/orderId={orderId}")
	public ResponseEntity<?> getOrderInfo(@PathVariable String orderId) {
		
		OrderInfoDto orderInfoDto = new OrderInfoDto();
		
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		this.orderService.getOrderInfo(orderInfoDto, orderId, yeeMessageDto);
		
		if (yeeMessageDto.getIsError()) {
			
			YeeMessageForm yeeMessageForm = this.mapper.map(yeeMessageDto, YeeMessageForm.class);
			
			return new ResponseEntity<>(yeeMessageForm,HttpStatus.OK);
		}

		return new ResponseEntity<>(orderInfoDto,HttpStatus.OK);
	}
	
	@GetMapping("/get-all-orders")
	public ResponseEntity<?> getAllOrder() {
		
		List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
		
		
		this.orderService.getAllOrder(orderInfoDtos);
		
		
		return new ResponseEntity<>(orderInfoDtos,HttpStatus.OK);
	}
	
	@GetMapping("/get-order-by-id")
	public ResponseEntity<?> getOrderById(HttpServletRequest request) {
		
		OrderInfoDto orderInfoDto = new OrderInfoDto();
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		String id = request.getParameter("orderId");
		this.orderService.getOrderInfo(orderInfoDto,id,yeeMessageDto);
		
		
		return new ResponseEntity<>(orderInfoDto,HttpStatus.OK);
	}
	
	@GetMapping("/cancel-order")
	public ResponseEntity<?> cancelOrder(HttpServletRequest request) {
		
		YeeMessage yeeMessage = new YeeMessage();
		Boolean updateOk = this.orderService.updateOrderStatus(request,yeeMessage,false);

		if (!updateOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
}
