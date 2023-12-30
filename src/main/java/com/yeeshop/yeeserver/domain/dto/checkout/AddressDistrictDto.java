package com.yeeshop.yeeserver.domain.dto.checkout;

import java.util.List;

import lombok.Data;

@Data 
public class AddressDistrictDto {

	private Integer DistrictID;
	
	private Integer ProvinceID;
	
    private String DistrictName;
    
    private String Code;
    
    private Integer Type;
    
    private Integer SupportType;
}
