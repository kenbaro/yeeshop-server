package com.yeeshop.yeeserver.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.yeeshop.yeeserver.app.form.SignUpForm;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.signup.SignUpDto;
import com.yeeshop.yeeserver.domain.service.ISignUpService;

import jakarta.servlet.http.HttpServletRequest;



/**
 * Controller for Sign Up Feature.
 * 
 * @author Thai Duy Bao
 * @since 2023
 */
@RestController
@RequestMapping("/customer")
@CrossOrigin
public class SignUpApi {

	@Autowired 
	private ModelMapper mapper;
	
	/** Interface for SignUp Feature. */
	@Autowired
	private ISignUpService signUpService;
	
	/** */
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpForm signUpForm) {
		
		// Data Transfer Object for Login.
		SignUpDto signUpDto = new SignUpDto();
		
		// map Form to DTO.
		signUpDto = this.mapper.map(signUpForm, SignUpDto.class);
		
		// A common Data Transform Object to show Message
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		// signUp Method.
		this.signUpService.signUp(signUpDto, yeeMessageDto);
		
		if (yeeMessageDto.getIsError()) {
			
			return new ResponseEntity<>(yeeMessageDto, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(signUpDto.getEmail(), HttpStatus.OK);
	}
	
	@PostMapping("/get-email")
	public ResponseEntity<?> getMail(@RequestBody String email) {
		
		Gson gson = new Gson();
		
		String formatEmail = gson.fromJson(email,String.class);
		
		// validate email Method.
		Boolean chk = this.signUpService.getMail(formatEmail);
		
		if (false == chk) {
			
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@GetMapping("/activate")
	public ResponseEntity<?> validateUser(HttpServletRequest request) {
		
//		Gson gson = new Gson();
//		
//		String otpFormat = gson.fromJson(otp,String.class);
		
		Boolean active = this.signUpService.activeUser(request);
		
		if (false == active) {
			
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
