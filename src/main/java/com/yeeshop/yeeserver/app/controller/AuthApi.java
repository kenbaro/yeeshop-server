package com.yeeshop.yeeserver.app.controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.AuthResponseForm;
import com.yeeshop.yeeserver.app.form.InfoForm;
import com.yeeshop.yeeserver.app.form.LoginForm;
import com.yeeshop.yeeserver.app.form.Oauth2RequestForm;
import com.yeeshop.yeeserver.app.form.common.YeeMessageForm;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeError;
import com.yeeshop.yeeserver.domain.dto.auth.InfoDto;
import com.yeeshop.yeeserver.domain.dto.auth.LoginDto;
import com.yeeshop.yeeserver.domain.dto.auth.OAuth2RequestDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.MailInfo;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import com.yeeshop.yeeserver.domain.service.ILoginService;
import com.yeeshop.yeeserver.domain.service.IMailService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
/**
 * A Control Class for control Login Api.
 * @author Thai Duy Bao.
 *
 * since 2023
 */
@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthApi {
	
	/** Performs object mapping. */
	@Autowired 
	private ModelMapper mapper;

	@Autowired
	private ILoginService loginService;
	
	@Autowired IMailService mailService;
	
	@Autowired IUserService userService;

	@GetMapping("/login")
	@ResponseBody
	public ResponseEntity<LoginDto> getLogin() {
		
		// A Data Transfer Object for Login.
		LoginDto loginDto = new LoginDto();
		
		
//		loginDto.setEmail("nptkeii@gmail.com");
//		loginDto.setPassword("1234567");

		return ResponseEntity.ok(loginDto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> postLogin (@RequestBody LoginForm loginForm) {
		
		// A Data Transfer Object for Login.
		LoginDto loginDto = new LoginDto();
		
		AuthResponseForm authResponseForm = new AuthResponseForm();
		
		// map Form to DTO.
		loginDto = this.mapper.map(loginForm, LoginDto.class);
		
		loginDto.setRole(YeeCommonConst.YeeRole.USER_ROLE);
		
		// A common Data Transform Object to show Message
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		// Login Method.
		this.loginService.editLogin(loginDto,yeeMessageDto);
		
		if (yeeMessageDto.getIsError()) {
			
			YeeMessageForm yeeMessageForm = new YeeMessageForm();
			this.mapper.map(yeeMessageDto, yeeMessageForm);
			
			return new ResponseEntity<>(yeeMessageForm, HttpStatus.OK);
		}

		// Map DTO to Form
		this.mapper.map(loginDto, authResponseForm);
		
		return new ResponseEntity<>(authResponseForm, HttpStatus.OK);
	}
	
	@PostMapping("/login-google")
    public ResponseEntity<?> loginGoogle (@RequestBody Oauth2RequestForm oAuth2Request){ 

		OAuth2RequestDto oAuth2RequestDto = this.mapper.map(oAuth2Request, OAuth2RequestDto.class);
		
		String accessToken = this.loginService.accessToken(oAuth2RequestDto);
		// A Data Transfer Object for Login.
		LoginDto loginDto = new LoginDto();
		// A common Data Transform Object to show Message
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		if (YeeStringUtils.isNoneEmpty(accessToken)) {
			
			User userDetails = this.loginService.getUserOAuthInfo(accessToken);
			
			this.loginService.editLoginOAuth2(loginDto,yeeMessageDto, accessToken, userDetails);
		} else {
			
			yeeMessageDto.setIsError(true);
			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Đã xảy ra lỗi trông quá trình thanh toán, vui lòng thử lại !");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
		}
		
		if (yeeMessageDto.getIsError()) {
			
			YeeMessageForm yeeMessageForm = new YeeMessageForm();
			this.mapper.map(yeeMessageDto, yeeMessageForm);
			
			return new ResponseEntity<>(yeeMessageForm, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		AuthResponseForm loginForm = this.mapper.map(loginDto, AuthResponseForm.class);
		return new ResponseEntity<>(loginForm, HttpStatus.OK);
	}
	
	@PostMapping("/admin-login")
	public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
		
		// A Data Transfer Object for Login.
		LoginDto loginDto = new LoginDto();
		
		AuthResponseForm authResponseForm = new AuthResponseForm();
		
		// map Form to DTO.
		loginDto = this.mapper.map(loginForm, LoginDto.class);
		
		loginDto.setRole(YeeCommonConst.YeeRole.ADMIN_ROLE);
		
		// A common Data Transform Object to show Message
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		
		// Login Method.
		this.loginService.editLogin(loginDto,yeeMessageDto);
		
		if (yeeMessageDto.getIsError()) {
			
			YeeMessageForm yeeMessageForm = new YeeMessageForm();
			this.mapper.map(yeeMessageDto, yeeMessageForm);
			
			return new ResponseEntity<>(yeeMessageForm, HttpStatus.UNAUTHORIZED);
		}

		// Map DTO to Form
		this.mapper.map(loginDto, authResponseForm);
				
		return new ResponseEntity<>(authResponseForm, HttpStatus.OK);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout() {
		
		SecurityContextHolder.clearContext();
		
		return new ResponseEntity<>("/",HttpStatus.OK);
	}
	
	@PostMapping("/add-new-user")
	public ResponseEntity<?> addNewUser(@RequestBody InfoDto infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		YeeMessage yeeMessage = new YeeMessage();
		
		yeeMessage.setMessageTitle(YeeStringUtils.EMPTY);
		
		try {
			infoDto.setRole("3");
			Boolean chk = this.userService.registNewUser(infoDto,yeeMessage);
			
			if (!chk) {
				
				if (!YeeStringUtils.isEmpty(yeeMessage.getMessageTitle())) {
					
					return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
				}
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào, không thể thêm người dùng mới!",HttpStatus.BAD_REQUEST);
			}
			
			String body = "\r\n" + "\r\n" + " <span>Đăng ký thành công người dùng: <h4>"+infoDto.getFullNm()+"</h4>Từ bây giờ bạn có thể đăng nhập và thực hiện các giao dịch mua sắm trên website của chúng tôi. Thông tin đăng nhập của bạn là: <h4>Email: "+infoDto.getEmail()+"</h4>Mật khẩu: "+infoDto.getPassWord()+"</h4>Xin cảm ơn.</span> \r\n"
					+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/login'>Đăng nhập ngay tại đây</a></h4> \r\n";
			
			this.sendMail(infoDto.getEmail(), body, "Xiaoyi Shop thông báo v/v đăng ký tài khoản:");
			
			return new ResponseEntity<>("Thêm người dùng thành công!",HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau!",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendMail(String email,String body, String title) throws MessagingException, IOException {
		
		mailService.queue(email, title, body);
		this.mailService.send(new MailInfo(email, title, body));
	}
	
}
