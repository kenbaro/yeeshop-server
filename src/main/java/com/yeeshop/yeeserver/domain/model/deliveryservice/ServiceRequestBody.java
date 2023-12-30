package com.yeeshop.yeeserver.domain.model.deliveryservice;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServiceRequestBody {
	
	private Integer shop_id;
	private Integer from_district;
	private Integer to_district;
}
