package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.google.gson.Gson;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeError;
import com.yeeshop.yeeserver.domain.dto.auth.LoginDto;
import com.yeeshop.yeeserver.domain.dto.auth.OAuth2Body;
import com.yeeshop.yeeserver.domain.dto.auth.OAuth2RequestDto;
import com.yeeshop.yeeserver.domain.dto.common.BearerToken;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.security.CustomUserDetails;
import com.yeeshop.yeeserver.domain.security.JwtUtilities;
import com.yeeshop.yeeserver.domain.security.oauth2.CustomOAuth2UserService;
import com.yeeshop.yeeserver.domain.service.ILoginService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

/**
 * Service Class For Login Feature.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Service
public class LoginServiceImpl implements ILoginService{
	
	/** An User Logic Interface. */
	@Autowired
	private IUserService userService ;
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	private JwtUtilities tokenProvider; 

	/** 
	 * @method Login Method.
	 * 
	 * 	@param loginDto - A Data Transfer Object for Login.
	 * */
	@Override
	public void editLogin(final LoginDto loginDto,final YeeMessageDto yeeMessageDto) {
		
		// get token
		String token = this.userService.authenticate(loginDto, yeeMessageDto);
		
		BearerToken bearerToken = new BearerToken(null, null);
		
		if (!yeeMessageDto.getIsError() && !YeeStringUtils.isEmpty(token)) {

			bearerToken.setAccessToken(token);
		}
		
		
		loginDto.setBearerToken(bearerToken);

	}

	@Override
	public String accessToken(OAuth2RequestDto oAuth2RequestDto) {

		String apiUrl = "https://accounts.google.com/o/oauth2/token";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Access-Control-Allow-Origin", "*");
		String requestBody = createTokenExchangeRequest(oAuth2RequestDto);
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(
		        apiUrl,HttpMethod.POST,
		        entity,
		        String.class
		);

		String accessToken = YeeStringUtils.EMPTY;
		//OAuth2AccessTokenResponse accessTokenResponse = responseEntity.getBody();
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			
			String json = responseEntity.getBody();
			Gson gson = new Gson();
			OAuth2Body obj = gson.fromJson(json, OAuth2Body.class);
			accessToken = obj.getAccess_token();
		}
		return accessToken;
	}
	
	private String createTokenExchangeRequest(OAuth2RequestDto oAuth2RequestDto) {
		
		oAuth2RequestDto.setClient_id("789104893881-uqvlf9p6gqspts1c07b73sk6vn6jqkcs.apps.googleusercontent.com");
		oAuth2RequestDto.setClient_secret("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");
		oAuth2RequestDto.setGrant_type("authorization_code");
		oAuth2RequestDto.setRedirect_uri("http://localhost:3001/oauth2/redirect");
		
		Gson gson = new Gson();
		
		return gson.toJson(oAuth2RequestDto);
	}

	@Override
	public User getUserOAuthInfo(String accessToken) {
		
		String apiUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Access-Control-Allow-Origin", "*");
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
		
		User user = new User();
		
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			
			String json = responseEntity.getBody();
			Gson gson = new Gson();
			CustomUserDetails customUserDetails = gson.fromJson(json, CustomUserDetails.class);
			
			user = this.customOAuth2UserService.upsertUser(customUserDetails);

		} else {
			user = null;
		}

		return user;
	}

	@Override
	public void editLoginOAuth2(LoginDto loginDto, YeeMessageDto yeeMessageDto,String accessToken, User user) {
		
		BearerToken bearerToken = new BearerToken(null, null);
		
		if (null != user) {
			
			loginDto.setEmail(user.getEmail());
			loginDto.setFullNm(user.getUserNm());
			
			List<String> roles = new ArrayList<>();
			roles.add(user.getRole().getRoleName());
			String token = this.tokenProvider.generateToken(user.getEmail(), roles);
			bearerToken.setAccessToken(token);
			loginDto.setBearerToken(bearerToken);
		} else {
			
			yeeMessageDto.setIsError(true);
			
			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Đã xảy ra lỗi trông quá trình đăng nhập, vui lòng thử lại !");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
		}
		
		
	}
	

}
