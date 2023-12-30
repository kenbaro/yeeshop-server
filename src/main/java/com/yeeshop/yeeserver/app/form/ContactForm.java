package com.yeeshop.yeeserver.app.form;

import lombok.Data;

/**
 * A Form Object for Contact Api.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
public class ContactForm {

	private String address;
	
	private String shopTel;
	
	private String email;
	
	private String workTime;
}
