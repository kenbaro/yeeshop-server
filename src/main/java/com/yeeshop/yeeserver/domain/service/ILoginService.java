package com.yeeshop.yeeserver.domain.service;

import com.yeeshop.yeeserver.domain.dto.auth.LoginDto;
import com.yeeshop.yeeserver.domain.dto.auth.OAuth2RequestDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.User;


/**
 * Interface for Login Feature.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
public interface ILoginService {
	
	/** Login Method.
	 * 
	 * 	@param loginDto - A Data Transfer Object for Login.
	 *  @param yeeMessageDto - A common Data Transform Object to show Message.
	 * */
	void editLogin(LoginDto loginDto, YeeMessageDto yeeMessageDto);
	
	String accessToken(OAuth2RequestDto oAuth2RequestDto);
	
	User getUserOAuthInfo(String accesToken);
	
	void editLoginOAuth2(LoginDto loginDto, YeeMessageDto yeeMessageDto, String accessToken, User user);
}
