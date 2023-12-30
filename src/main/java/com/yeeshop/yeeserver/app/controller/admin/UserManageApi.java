package com.yeeshop.yeeserver.app.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.InfoForm;
import com.yeeshop.yeeserver.domain.dto.auth.InfoDto;
import com.yeeshop.yeeserver.domain.dto.auth.PasswordDto;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.MailInfo;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.IMailService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class UserManageApi {

	@Autowired 
	private ModelMapper mapper;
	
	@Autowired
	private IUserService userService;
	
	@Autowired IMailService mailService;
	
	@PostMapping("/update-info-admin")
	public ResponseEntity<?> updateProfileAdmin(@RequestBody InfoForm infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		try {
			String msg = YeeStringUtils.EMPTY;
			Boolean chk = this.userService.updateAdminInfo(infoDto, msg);
			
			if (!chk) {
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào, không thể cập nhật thông tin cá nhân",HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return new ResponseEntity<>("Cập nhật thành công!",HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau!",HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping("/get-user-info")
	public ResponseEntity<?> getAdminProfile(@RequestBody InfoForm infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		try {
			String msg = YeeStringUtils.EMPTY;
			Boolean chk = this.userService.changeAdminInfo(infoDto, msg);
			
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
	
	@PostMapping("/get-user-info-v2")
	public ResponseEntity<?> getUserProfilev2(@RequestBody InfoDto infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		try {
			String msg = YeeStringUtils.EMPTY;

			Boolean chk = this.userService.changeAdminInfo(infoDto, msg);
			
			if (!chk) {
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào",HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			return new ResponseEntity<>(infoDto,HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!",HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping("/update-user-info-v2")
	public ResponseEntity<?> updateUserInfo(@RequestBody InfoDto infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		YeeMessage yeeMessage = new YeeMessage();
		
		yeeMessage.setMessageTitle(YeeStringUtils.EMPTY);
		
		try {

			Boolean chk = this.userService.updateUserAdminInfo(infoDto, yeeMessage);
			
			if (!chk) {
				
				if (!YeeStringUtils.isEmpty(yeeMessage.getMessageTitle())) {
					
					return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
				}
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào, không thể cập nhật người dùng !",HttpStatus.BAD_REQUEST);
			}
			
//			String body = "\r\n" + "\r\n" + " <span>Quản trị viên vừa cập nhật thông thành công cho: </span> <h4>Người dùng: "+infoDto.getFullNm()+"</h4><h4>Email: "+infoDto.getEmail()+" </h4><span>Xin cảm ơn.</span> \r\n"
//					+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/login'>Đăng nhập ngay tại đây</a></h4> \r\n";
//			
//			this.sendMail(infoDto.getEmail(), body, "Xiaoyi Shop thông báo v/v đăng ký tài khoản:");
			
			return new ResponseEntity<>("Cập nhật thông tin thành công!",HttpStatus.OK);
			
		} catch (Exception e) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!",HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auth-admin")
	public ResponseEntity<?> authenAdmin(HttpServletRequest httpServletRequest) {
		
		Boolean auth = this.userService.authenAdmin(httpServletRequest);
		if (auth == true) {
			
			return new ResponseEntity<>("Xác thực thành công",HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Lỗi xác thực !",HttpStatus.UNAUTHORIZED);
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordDto pwDto) {
		
		String msg = this.userService.changePassword(pwDto);
		
		if (!YeeStringUtils.isEmpty(msg)) {
			
			return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>("Cập nhật mật khẩu thành công. Vui lòng đăng nhập lại!", HttpStatus.OK);
	}
	
	@GetMapping("/get-all-users")
	public ResponseEntity<?> getAllUsers() {
		
		List<InfoDto> infoUsers = new ArrayList<>();
		
		this.userService.getAllUsers(infoUsers);

		
		return new ResponseEntity<>(infoUsers,HttpStatus.OK);
	}
	
	@PostMapping("/delete-user")
	public ResponseEntity<?> deleteUser(@RequestBody List<String> emails) {
		
		String msg = YeeStringUtils.EMPTY;
		
		Boolean deleteSuccess = this.userService.deleteUserByEmail(emails);
		
		if (!deleteSuccess) {
			
			msg = "Xoá người dùng không thành công! vui lòng thử lại!";
			return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		msg = "Xoá người dùng thành công !";
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	
	@GetMapping("/active-user-by-admin")
	public ResponseEntity<?> activeUserByAdmin(HttpServletRequest request) throws MessagingException, IOException {
		
		String msg = YeeStringUtils.EMPTY;
		String email = request.getParameter("email");
		Boolean activeSucess = this.userService.editActiveUserByAdmin(email,msg,true);
		
		if (!activeSucess) {
			
			return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
		}
		
		String body = "\r\n" + "\r\n" + " <span>Email của bạn đã được kích hoạt bởi Quản Trị Viên. Từ bây giờ bạn có thể đăng nhập và thực hiện các giao dịch mua sắm trên website của chúng tôi. </span> \r\n"
				+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/login'>Đăng nhập ngay</a></h4> \r\n";
		this.sendMail(email,body,"Xiaoyi Shop thông báo cập nhật tài khoản:");
		return new ResponseEntity<>("Kích hoạt tài khoản thành công",HttpStatus.OK);
	}
	
	@GetMapping("/deactive-user-by-admin")
	public ResponseEntity<?> deactiveUserByAdmin(HttpServletRequest request) throws MessagingException, IOException {
		
		String msg = YeeStringUtils.EMPTY;
		String email = request.getParameter("email");
		Boolean activeSucess = this.userService.editActiveUserByAdmin(email,msg,false);
		
		if (!activeSucess) {
			
			return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
		}
		
		String body = "\r\n" + "\r\n" + " <span>Email của bạn đã bị vô hiệu hoá bởi Quản Trị Viên. Để kích hoạt lại email, vui lòng truy cập link dưới đây để kích hoạt lại email.</span> \r\n"
				+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/send-otp/"+email+"'>Truy cập tại đây</a></h4> \r\n";
		
		this.sendMail(email,body,"Xiaoyi Shop thông báo cập nhật tài khoản:");
		return new ResponseEntity<>("Vô hiệu hoá tài khoản thành công",HttpStatus.OK);
	}

	@PostMapping("/add-new-user")
	public ResponseEntity<?> addNewUser(@RequestBody InfoDto infoForm) {
		
		InfoDto infoDto = this.mapper.map(infoForm, InfoDto.class);
		
		YeeMessage yeeMessage = new YeeMessage();
		
		yeeMessage.setMessageTitle(YeeStringUtils.EMPTY);
		
		try {

			Boolean chk = this.userService.addNewUser(infoDto,yeeMessage);
			
			if (!chk) {
				
				if (!YeeStringUtils.isEmpty(yeeMessage.getMessageTitle())) {
					
					return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
				}
				
				return new ResponseEntity<>("Lỗi thông tin đầu vào, không thể thêm người dùng mới!",HttpStatus.BAD_REQUEST);
			}
			
			String body = "\r\n" + "\r\n" + " <span>Quản trị viên vừa đăng ký thành công cho người dùng: <h4>"+infoDto.getFullNm()+"</h4>Từ bây giờ bạn có thể đăng nhập và thực hiện các giao dịch mua sắm trên website của chúng tôi. Thông tin đăng nhập của bạn là: <h4>Email: "+infoDto.getEmail()+"</h4>Mật khẩu: "+infoDto.getPassWord()+"</h4>Xin cảm ơn.</span> \r\n"
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
