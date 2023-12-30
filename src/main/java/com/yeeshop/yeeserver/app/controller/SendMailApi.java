package com.yeeshop.yeeserver.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.service.IMailService;
import com.yeeshop.yeeserver.domain.service.ISignUpService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

@CrossOrigin("*")
@RestController
@RequestMapping("/customer")
public class SendMailApi {

	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired ISignUpService signUpService;
	
	@PostMapping("send-otp")
	public ResponseEntity<Integer> sendMailOtp(@RequestBody String email) {
		
		
		Gson gson = new Gson();
		
		String formatEmail = gson.fromJson(email,String.class);
		
		int randomOtp = this.signUpService.getOtp(formatEmail);
		
		if (0 == randomOtp) {
			return ResponseEntity.notFound().build();
		}
		//String otpEncoder = this.userService.encodeStr(String.valueOf(randomOtp));
		sendMailOtp(email, randomOtp, "Xác nhận tài khoản!");
		return ResponseEntity.ok().build();
	}
	
	private void sendMailOtp(String email, int randomOtp, String title) {
		String body = "\r\n" + "    <h2>Mã OTP của quý khách là: " + randomOtp + "</h2>\r\n"
				+ "\r\n" + "    <h4>Mã OTP chỉ có hiệu lực trong vòng 5 phút, nếu OTP hết hạn, cảm phiền quý khách kích hoạt lại.</h4> \r\n";
		mailService.queue(email, title, body);
		this.mailService.run();
	}
	
	@PostMapping("/send-mail-forgot-password")
	public ResponseEntity<String> sendToken(@RequestBody String email) {

		Gson gson = new Gson();
		
		String formatEmail = gson.fromJson(email,String.class);
		
		User user = this.userService.findByEmail(formatEmail);
		if (null == user) {
			return ResponseEntity.notFound().build();
		}

		int randomOtp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
		String newPwStr = "Xiaoyi" + String.valueOf(randomOtp);
		this.sendMailPw(email, newPwStr, "Reset mật khẩu");
		
		String pwEncode = this.userService.encodeStr(newPwStr);
		
		if (YeeStringUtils.isEmpty(pwEncode)) {
			
			return ResponseEntity.notFound().build();
		}
		user.setUserPw(pwEncode);
		
	    user = this.userService.saverUser(user);
	    
	    if (null == user) {

			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();

	}
	
	public void sendMailPw(String email, String newPwStr, String title) {
		String body = "\r\n" + "    <h2>Mật khẩu mới của bạn là: " + newPwStr + "</h2>\r\n"
				+ "\r\n" + "    <h5>Đăng nhập bằng mật khẩu mới và thay đổi mật khẩu ngay bạn nhé!  </h5> \r\n"
				+ "\r\n" + "    <h5>Đăng nhập <a href='http://localhost:3001/login'> tại đây</a>  </h5> \r\n";
		this.mailService.queue(email, title, body);
		this.mailService.run();
	}
}
