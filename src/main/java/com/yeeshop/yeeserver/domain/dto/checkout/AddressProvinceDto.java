package com.yeeshop.yeeserver.domain.dto.checkout;

import lombok.Data;

@Data
public class AddressProvinceDto {

	private Integer ProvinceID;
	
	private String ProvinceName;
	
	private Integer CountryID;
	
	private String Code;
}
