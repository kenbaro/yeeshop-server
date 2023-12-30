package com.yeeshop.yeeserver.app.controller.admin;

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
import com.yeeshop.yeeserver.domain.dto.productgroup.ProductGroupDto;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.IProductGroupService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class ProductGroupApi {

	@Autowired private IProductGroupService productGroupService;
	
	@Autowired private ModelMapper mapper;

	@GetMapping("/get-all-product-group")
	public ResponseEntity<?> getAllProductGroup() {

		List<ProductGroupDto> productGroupDtos = this.productGroupService.getAllProductGroup();
		return new ResponseEntity<>(productGroupDtos,HttpStatus.OK);
	}
	
	@PostMapping("/get-product-group-cate")
	public ResponseEntity<?> getCate(HttpServletRequest httpServletRequest,@RequestBody String productGroupForm) {
		
		Gson gson = new Gson();
		ProductGroupDto productGroupDto = gson.fromJson(productGroupForm, ProductGroupDto.class);
		
		this.productGroupService.getAllMainCategory(httpServletRequest, productGroupDto);
		return new ResponseEntity<>(productGroupDto,HttpStatus.OK);
	}
	
	@GetMapping("/get-product-group-cate-by-id")
	public ResponseEntity<?> getProductGroupById(HttpServletRequest httpServletRequest) {
		
		ProductGroupDto productGroupDto = new ProductGroupDto();
		this.productGroupService.getProductGroupById(httpServletRequest, productGroupDto);
		return new ResponseEntity<>(productGroupDto,HttpStatus.OK);
	}
	
	@GetMapping("/get-all-subcate")
	public ResponseEntity<?> getAllSubCate(HttpServletRequest httpServletRequest) {
		
		ProductGroupDto productGroupDto = new ProductGroupDto();
		this.productGroupService.getAllSubCategory(httpServletRequest, productGroupDto);
		return new ResponseEntity<>(productGroupDto,HttpStatus.OK);
	}
	
	@PostMapping("/add-product-group-cate")
	public ResponseEntity<?> addCate(HttpServletRequest httpServletRequest,@RequestBody String productGroupForm) {
		
		Gson gson = new Gson();
		ProductGroupDto productGroupDto = gson.fromJson(productGroupForm, ProductGroupDto.class);
		//ProductGroupDto productGroupDto = this.mapper.map(productGroupForm, ProductGroupDto.class);
		YeeMessage yeeMessage = new YeeMessage();
		Boolean excuteOk = this.productGroupService.addCate(httpServletRequest, productGroupDto, yeeMessage);
		
		if (!excuteOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
	
	@PostMapping("/update-product-group-cate")
	public ResponseEntity<?> updateCate(@RequestBody String productGroupForm) {
		
		Gson gson = new Gson();
		ProductGroupDto productGroupDto = gson.fromJson(productGroupForm, ProductGroupDto.class);
		//ProductGroupDto productGroupDto = this.mapper.map(productGroupForm, ProductGroupDto.class);
		YeeMessage yeeMessage = new YeeMessage();
		Boolean excuteOk = this.productGroupService.updateCate(productGroupDto, yeeMessage);
		
		if (!excuteOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
	
	@GetMapping("/delete-product-group-cate")
	public ResponseEntity<?> deleteCate(HttpServletRequest httpServletRequest) {

		YeeMessage yeeMessage = new YeeMessage();
		Boolean excuteOk = this.productGroupService.deleteCate(httpServletRequest, yeeMessage);
		
		if (!excuteOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
}
