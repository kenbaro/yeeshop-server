package com.yeeshop.yeeserver.domain.model.fee;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliveryFee {

	private Integer code;
	
	private String message;
	
	private FeeData data; 
}
