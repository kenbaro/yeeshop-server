package com.yeeshop.yeeserver.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.SingleProductForm;
import com.yeeshop.yeeserver.domain.dto.singleproduct.AutoCompleteProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductDto;
import com.yeeshop.yeeserver.domain.service.ISingleProductService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;
/**
 * Controller for SingleProduct API.
 * 
 * @author Thai Duy Bao
 * @since 2023
 */
@RestController
@RequestMapping("/customer")
@CrossOrigin
public class SingleProductApi {

	@Autowired ISingleProductService productService;
	
	@Autowired ModelMapper mapper;
	
	@GetMapping("/getSingleProductApi/sku={SKU}/storage={storage}/color={color}")
	public ResponseEntity<?> getSingleProductApi(@PathVariable String SKU, @PathVariable String storage, @PathVariable String color) throws Exception {
		
		SingleProductDto singleProductDto = new SingleProductDto();
		
		if (YeeStringUtils.isNotEmpty(SKU)) {
			
			this.productService.initProduct(singleProductDto, SKU);
		}
		
		this.productService.getProduct(singleProductDto, storage, color);
		
		this.productService.getRelatedProduct(singleProductDto);
		SingleProductForm singleProductForm = this.mapper.map(singleProductDto, SingleProductForm.class);

		return new ResponseEntity<>(singleProductForm,HttpStatus.OK);
	}
	
	@PostMapping("/validateProduct")
	public ResponseEntity<?> validateProduct(@RequestBody SingleProductForm singleProductForm) {
		
		SingleProductDto singleProductDto = this.mapper.map(singleProductForm, SingleProductDto.class);
		
		Boolean validate = this.productService.validateProduct(singleProductDto);
		return new ResponseEntity<>(validate, HttpStatus.OK);
	}
	
	@GetMapping("/product-auto-complete/keyword={keyword}")
	public ResponseEntity<?> autoCompleteProduct(@PathVariable String keyword) {
		
		AutoCompleteProductDto autoCompleteProductDto = new AutoCompleteProductDto();
		
		this.productService.initAutoComplete(autoCompleteProductDto, keyword);
		
		return new ResponseEntity<>(autoCompleteProductDto,HttpStatus.OK);
	}
	
	@GetMapping("/product-detail-filter")
	public ResponseEntity<?> productDetailFilter(HttpServletRequest request) throws Exception {
		
		SingleProductDto singleProductDto = new SingleProductDto();
		String color = request.getParameter("color");
		String storage = request.getParameter("storage");
		
		this.productService.getProduct(singleProductDto, storage, color);
		
		SingleProductForm singleProductForm = this.mapper.map(singleProductDto, SingleProductForm.class);

		return new ResponseEntity<>(singleProductForm,HttpStatus.OK);
	}
}
