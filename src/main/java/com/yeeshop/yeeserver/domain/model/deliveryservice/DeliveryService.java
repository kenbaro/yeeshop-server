package com.yeeshop.yeeserver.domain.model.deliveryservice;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliveryService {

	private Integer code;
	
	private List<ServiceData> data;
	
	private String message;
}
