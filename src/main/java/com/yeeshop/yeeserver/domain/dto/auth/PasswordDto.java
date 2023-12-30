package com.yeeshop.yeeserver.domain.dto.auth;

import lombok.Data;

@Data
public class PasswordDto {
	
	private String oldPassWord;
	
	private String newPassWord;
	
	private String confirmPassWord;

}
