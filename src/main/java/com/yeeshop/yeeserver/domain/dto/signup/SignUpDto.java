package com.yeeshop.yeeserver.domain.dto.signup;

import lombok.Data;

/**
 * Data Transfer Object for Login.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
public class SignUpDto {

	/** fullName. */
	private String fullName;
	
	/** email. */
	private String email;
	
	/** phone*/
	private String phone;
	
	/** password*/
	private String passWord;
	
	/** confirmPassWord*/
	private String confirmPassword;
}
