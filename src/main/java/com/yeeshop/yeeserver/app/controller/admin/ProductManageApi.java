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
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yeeshop.yeeserver.domain.admin.dto.productmanage.EditProductDto;
import com.yeeshop.yeeserver.domain.admin.dto.productmanage.SubEditProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.ProductInfoDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductDto;
import com.yeeshop.yeeserver.domain.model.MailInfo;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.IMailService;
import com.yeeshop.yeeserver.domain.service.ISingleProductService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class ProductManageApi {

	@Autowired
	private ISingleProductService singleProductService;
	
	@Autowired private IMailService mailService;
	
	@Autowired private ModelMapper mapper;

	@GetMapping("get-all-products")
	public ResponseEntity<?> getAllProducts() {
		
		List<ProductInfoDto> singleProductDtos = this.singleProductService.getAllProduct(null);
		return new ResponseEntity<> (singleProductDtos,HttpStatus.OK);
	}
	
	@GetMapping("search-products-by-params")
	public ResponseEntity<?> getAllProducts(HttpServletRequest request) {
		
		List<ProductInfoDto> singleProductDtos = this.singleProductService.getAllProduct(request);
		return new ResponseEntity<> (singleProductDtos,HttpStatus.OK);
	}
	
	@GetMapping("get-products-by-nm")
	public ResponseEntity<?> getProductsByNm(HttpServletRequest request) {
		
		List<SubEditProductDto> singleProductDtos = this.singleProductService.getProductsByNm(request);
		return new ResponseEntity<> (singleProductDtos,HttpStatus.OK);
	}
	
	@GetMapping("/active-product-by-admin")
	public ResponseEntity<?> activeProductByAdmin(HttpServletRequest request) throws MessagingException, IOException {
		
		
		String sku = request.getParameter("sku");
		YeeMessage message = new YeeMessage();
		message.setMessageTitle(YeeStringUtils.EMPTY);
		Boolean activeSucess = this.singleProductService.editActiveProductByAdmin(sku,message,true);
		
		if (!activeSucess) {
			
			return new ResponseEntity<>(message.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		
//		String body = "\r\n" + "\r\n" + " <span>Email của bạn đã được kích hoạt bởi Quản Trị Viên. Từ bây giờ bạn có thể đăng nhập và thực hiện các giao dịch mua sắm trên website của chúng tôi. </span> \r\n"
//				+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/login'>Đăng nhập ngay</a></h4> \r\n";
//		this.sendMail(email,body,"Xiaoyi Shop thông báo cập nhật tài khoản:");
		return new ResponseEntity<>("Kích hoạt sản phẩm thành công",HttpStatus.OK);
	}
	
	@GetMapping("/deactive-product-by-admin")
	public ResponseEntity<?> deactiveProductByAdmin(HttpServletRequest request) throws MessagingException, IOException {
		
		String sku = request.getParameter("sku");
		YeeMessage message = new YeeMessage();
		message.setMessageTitle(YeeStringUtils.EMPTY);
		Boolean activeSucess = this.singleProductService.editActiveProductByAdmin(sku,message,false);
		
		if (!activeSucess) {
			
			return new ResponseEntity<>(message.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		
//		String body = "\r\n" + "\r\n" + " <span>Email của bạn đã bị vô hiệu hoá bởi Quản Trị Viên. Để kích hoạt lại email, vui lòng truy cập link dưới đây để kích hoạt lại email.</span> \r\n"
//				+ "\r\n" + " <h4><a href='http://192.168.1.21:3001/send-otp/"+email+"'>Truy cập tại đây</a></h4> \r\n";
//		
//		this.sendMail(email,body,"Xiaoyi Shop thông báo cập nhật tài khoản:");
		return new ResponseEntity<>("Vô hiệu hoá sản phẩm thành công",HttpStatus.OK);
	}
	
//	private void sendMail(String email,String body, String title) throws MessagingException, IOException {
//		
//		this.mailService.queue(email, title, body);
//		this.mailService.send(new MailInfo(email, title, body));
//	}

	@PostMapping("/get-product-by-admin")
	public ResponseEntity<?> editProductApi(@RequestBody String editProductFormData) throws MessagingException, IOException {
		
		
		Gson gson = new Gson();

		EditProductDto productDto = gson.fromJson(editProductFormData, EditProductDto.class);
		
		this.singleProductService.initProductDto(productDto);


		return new ResponseEntity<>(productDto,HttpStatus.OK);
	}
	
	@PostMapping("/get-product-new-by-admin")
	public ResponseEntity<?> editProductNewApi(@RequestBody String editProductFormData) throws MessagingException, IOException {
		
		
		Gson gson = new Gson();

		EditProductDto productDto = gson.fromJson(editProductFormData, EditProductDto.class);
		
		this.singleProductService.initProductDtoNew(productDto);

		return new ResponseEntity<>(productDto,HttpStatus.OK);
	}
	
	@PostMapping("/update-product-by-admin")
	public ResponseEntity<?> updateProductByAdmin(@RequestBody String editProductFormData) throws MessagingException, IOException {
		
		
		Gson gson = new Gson();

		EditProductDto productDto = gson.fromJson(editProductFormData, EditProductDto.class);
		
		YeeMessage yeeMessage = new YeeMessage();
		
		Boolean updateOK =  this.singleProductService.updateProduct(productDto, yeeMessage);

		if (!updateOK) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(productDto,HttpStatus.OK);
		
	}
	
	@PostMapping("/delete-products")
	public ResponseEntity<?> deleteProducts(@RequestBody String request) throws MessagingException, IOException {
		
		
		Gson gson = new Gson();

		List<String> idList = gson.fromJson(request, new TypeToken<List<String>>(){}.getType());
		
		YeeMessage yeeMessage = new YeeMessage();
		
		Boolean updateOK =  this.singleProductService.deleteProducts(idList, yeeMessage);

		if (!updateOK) {
			
			return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại",HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
		
	}
	
	
	
}
