package com.yeeshop.yeeserver.app.controller;

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
import com.yeeshop.yeeserver.domain.dto.auth.PasswordDto;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserApi {

	@Autowired
	private IUserService userService;
	
	@Autowired private ModelMapper mapper;
	
	@GetMapping("/get-me")
	public ResponseEntity<?> getMe() {
		
		InfoDto infoDto = this.userService.getUserInfo(YeeStringUtils.EMPTY);
		
		if (null == infoDto) {
			
			return new ResponseEntity<>(false, HttpStatus.OK);
		}

		return new ResponseEntity<>(infoDto, HttpStatus.OK);
	}
	
	@PostMapping("/change-info")
	public ResponseEntity<?> changeInfo(@RequestBody InfoDto infoDto) {
		
		//infoDto = new in 
		
		if (null == infoDto) {
			
			return new ResponseEntity<>("Thay đổi thông tin không thành công. Vui lòng thử lại !", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(infoDto, HttpStatus.OK);
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordDto pwDto) {
		
		String msg = this.userService.changePassword(pwDto);
		
		if (!YeeStringUtils.isEmpty(msg)) {
			
			return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>("Cập nhật mật khẩu thành công", HttpStatus.OK);
	}
	
	@PostMapping("/get-user-info")
	public ResponseEntity<?> getAdminProfile(@RequestBody InfoForm infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		try {
			String msg = YeeStringUtils.EMPTY;
			
			Boolean chk = this.userService.changeUserInfo(infoDto, msg);
			
			if (!chk) {
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào",HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			infoForm = new InfoForm();
			
			this.mapper.map(infoDto, infoForm);
			return new ResponseEntity<>(infoForm,HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!",HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/logout")
	public void logout() {
		
		SecurityContextHolder.clearContext();
	} 
	
	@PostMapping("/update-info-user")
	public ResponseEntity<?> updateProfileUser(@RequestBody InfoForm infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		try {
			String msg = YeeStringUtils.EMPTY;
			Boolean chk = this.userService.updateUserInfo(infoDto, msg);
			
			if (!chk) {
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào, không thể cập nhật thông tin cá nhân",HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return new ResponseEntity<>("Cập nhật thành công!",HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau!",HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
