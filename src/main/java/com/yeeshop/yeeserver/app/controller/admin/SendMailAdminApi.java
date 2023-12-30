package com.yeeshop.yeeserver.app.controller.admin;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yeeshop.yeeserver.domain.dto.auth.InfoDto;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.MailInfo;
import com.yeeshop.yeeserver.domain.service.IMailService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class SendMailAdminApi {

	@Autowired 
	private ModelMapper mapper;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/send-mail-info")
	public ResponseEntity<?> sendMailUpdateInfo(@RequestBody InfoDto infoDto) throws MessagingException, IOException {
		
		String body = "\r\n" + "\r\n" + " <span>Quản trị viên vừa cập nhật thông thành công cho: </span> <h4>Người dùng: "+infoDto.getFullNm()+"</h4><h4>Email: "+infoDto.getEmail()+" </h4><span>Xin cảm ơn.</span> \r\n"
				+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/login'>Đăng nhập ngay để kiểm tra thông tin tại đây</a></h4> \r\n";
		
		this.sendMail(infoDto.getEmail(), body, "Xiaoyi Shop thông báo v/v cập nhật tài khoản:");

		return new ResponseEntity<>(true,HttpStatus.OK);
	}
	
	@PostMapping("/send-mail-forgot-password")
	public ResponseEntity<String> sendToken(@RequestBody String email) {

		Gson gson = new Gson();
		
		String formatEmail = gson.fromJson(email,String.class);
		
		User user = this.userService.findByEmail(formatEmail);
		if (null == user) {
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình reset mật khẩu, vui lòng thử lại!", HttpStatus.BAD_REQUEST);
		}

		int randomOtp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
		String newPwStr = "Xiaoyi" + String.valueOf(randomOtp);
		this.sendMailPw(email, newPwStr, "Xiaoyi Shop thông báo v/v reset mật khẩu:");
		
		String pwEncode = this.userService.encodeStr(newPwStr);
		
		if (YeeStringUtils.isEmpty(pwEncode)) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình reset mật khẩu, vui lòng thử lại!", HttpStatus.BAD_REQUEST);
		}
		user.setUserPw(pwEncode);
		
	    user = this.userService.saverUser(user);
	    
	    if (null == user) {

	    	return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình reset mật khẩu, vui lòng thử lại!", HttpStatus.BAD_REQUEST);
		}

	    return new ResponseEntity<>("Reset mật khẩu thành công, mail thông báo đã được gửi tới người dùng", HttpStatus.OK);

	}
	
	private void sendMail(String email,String body, String title) throws MessagingException, IOException {
		
		mailService.queue(email, title, body);
		this.mailService.send(new MailInfo(email, title, body));
	}
	
	private void sendMailPw(String email, String newPwStr, String title) {
		String body = "\r\n" + "    <h2>Mật khẩu mới của bạn là: " + newPwStr + "</h2>\r\n"
				+ "\r\n" + "    <h5>Đăng nhập bằng mật khẩu mới và thay đổi mật khẩu ngay bạn nhé!  </h5> \r\n"
				+ "\r\n" + "    <h5>Đăng nhập <a href='http://localhost:3001/login'> tại đây</a>  </h5> \r\n";
		this.mailService.queue(email, title, body);
		this.mailService.run();
	}
}
