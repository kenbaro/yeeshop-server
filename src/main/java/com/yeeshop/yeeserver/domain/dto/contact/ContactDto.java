package com.yeeshop.yeeserver.domain.dto.contact;

import lombok.Data;

/**
 * Data Transfer Object for Contact Api.
 * 
 * @author Thai Duy Bao.
 * @since 2023
 */
@Data
public class ContactDto {

	private String address;
	
	private String shopTel;
	
	private String email;
	
	private String workTime;
}
