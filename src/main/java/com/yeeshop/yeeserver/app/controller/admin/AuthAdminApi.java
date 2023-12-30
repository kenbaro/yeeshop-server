package com.yeeshop.yeeserver.app.controller.admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.InfoForm;
import com.yeeshop.yeeserver.domain.dto.auth.InfoDto;
import com.yeeshop.yeeserver.domain.service.ILoginService;
import com.yeeshop.yeeserver.domain.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AuthAdminApi {
	
	@Autowired 
	private ModelMapper mapper;


	@Autowired
	private IUserService userService;
	
	@GetMapping("/get-user-info")
	public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
		
		String email = request.getParameter("email");

		InfoDto infoDto = this.userService.getUserInfo(email);
		
		if (null == infoDto) {
			
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(infoDto, HttpStatus.OK);
		
	}
	
	@PostMapping("/get-user-info-api")
	public ResponseEntity<?> getUserInfo(HttpServletRequest request,@RequestBody InfoForm infoForm) {
		
		
		String email = request.getParameter("email");

		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		if (null == infoDto) {
			
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(infoDto, HttpStatus.OK);
		
	}
	
	
}
