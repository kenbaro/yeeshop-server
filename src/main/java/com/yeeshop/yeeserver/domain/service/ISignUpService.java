package com.yeeshop.yeeserver.domain.service;

import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.signup.SignUpDto;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for SignUp Feature.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
public interface ISignUpService {

	/** signUp Method.
	 * 
	 * 	@param signUpDto - Data Transfer Object for Login..
	 *  @param yeeMessageDto - A common Data Transform Object to show Message.
	 */
	public void signUp(SignUpDto signUpDto, YeeMessageDto yeeMessageDto);
	
	public Boolean getMail(String email);
	
	public int getOtp(String email);
	
	public Boolean activeUser(HttpServletRequest request);
}
