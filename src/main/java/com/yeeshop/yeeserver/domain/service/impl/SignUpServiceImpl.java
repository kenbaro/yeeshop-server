package com.yeeshop.yeeserver.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.signup.SignUpDto;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.service.ISignUpService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Service Class For SignUp Feature.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Service
public class SignUpServiceImpl implements ISignUpService{
	
	/** Interface for User. */
	@Autowired
	private IUserService userService;

	/** signUp Method.
	 * 
	 * 	@param signUpDto - Data Transfer Object for Login..
	 *  @param yeeMessageDto - A common Data Transform Object to show Message.
	 */
	@Override
	public void signUp(SignUpDto signUpDto, YeeMessageDto yeeMessageDto) {
		
		// Method for SignUp.
		this.userService.register(signUpDto, yeeMessageDto);
	}

	@Override
	public Boolean getMail(String email) {
		
		User user = this.userService.findByEmail(email);
		
		if(null == user || user.getIsActivated()) {
			
			return false;

		}
		
		
		return true;
	}

	@Override
	public int getOtp(String email) {
		User user = this.userService.findByEmail(email);
		
		if (null == user) {
			return 0;
		} else {
			
			int otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
			
			String otpDate = YeeDateTimeUtils.getCurrentDateTime();
			
			user.setUserOtp(String.valueOf(otp));
			user.setDateOtp(otpDate);
			
			user = this.userService.saverUser(user);
			
			
			return null != user ? otp : 0;
		}
	}

	@Override
	public Boolean activeUser(HttpServletRequest request) {
		
		String otp = request.getParameter("otp");
		
		if (YeeStringUtils.isEmpty(otp)) {
			return false;
		}
		String email= request.getParameter("email");
		
		if (!YeeStringUtils.chkValidEmail(email)) {
			
			return false;
		}
		
		User user = this.userService.findByEmail(email);
		
		if (null == user || user.getIsActivated()) {
			
			return false;
		} else if (!YeeStringUtils.equals(otp, user.getUserOtp())) {
			
			return false;
		} 
		
		
		long minuteDif = YeeDateTimeUtils.calcMinute(user.getDateOtp());
		
		if (0 > minuteDif) {
			
			return false;
		} else {
			
			user.setIsActivated(true);
			
			user = this.userService.saverUser(user);
			
			return null == user ? false : true;
		}
	}

}
