package com.yeeshop.yeeserver.app.form;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.order.OrderDetailDto;

import lombok.Data;

@Data
public class OrderInfoForm {

	private String orderId;
	
	private String orderUser;
	
	private String orderRPeople;
	
	private String orderStatus;
	
	private String orderDate;
	
	private String rPeopleTel;
	
	private String amount;
	
	private String notFeePrice;
	
	private String shipFee;
	
	private String orderDspt;
	
	private String payMethod;
	
	private String delivery;
	
	private String deliveryCode;
	
	private String orderAddress;
	
	private String orderDetailAddress;
	
	private String transactionNo;
	
	private String transactionStatus;
	
	private List<OrderDetailDto> details;
}
