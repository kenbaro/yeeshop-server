package com.yeeshop.yeeserver.app.form;

import lombok.Data;

/**
 * Form Object for Signup.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
public class SignUpForm {

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
