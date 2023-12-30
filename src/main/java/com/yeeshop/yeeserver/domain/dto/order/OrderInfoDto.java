package com.yeeshop.yeeserver.domain.dto.order;

import java.util.List;

import lombok.Data;

@Data
public class OrderInfoDto {

	private String orderId;
	
	private String orderUser;
	
	private String userMail;

	private String orderRPeople;
	
	private String orderStatus;
	
	private String orderStatusCd;
	
	private String orderDate;
	
	private String rPeopleTel;
	
	private String amount;
	
	private String notFeePrice;
	
	private String shipFee;
	
	private String orderDspt;
	
	private String payMethod;
	
	private Integer payType;
	
	private String delivery;
	
	private String deliveryCode;
	
	private String orderAddress;
	
	private String orderDetailAddress;
	
	private String transactionNo;
	
	private String transactionStatus;
	
	private List<OrderDetailDto> details;
}
