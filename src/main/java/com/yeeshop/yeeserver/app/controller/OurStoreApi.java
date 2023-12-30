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

import com.yeeshop.yeeserver.app.form.StoreForm;
import com.yeeshop.yeeserver.domain.dto.ourstore.StoreDto;
import com.yeeshop.yeeserver.domain.service.IOurStoreService;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class OurStoreApi {

	@Autowired
	private IOurStoreService ourStoreService;
	
	@Autowired 
	private ModelMapper mapper;

	@GetMapping("/allProduct/keyWord={keyWord}")
	public ResponseEntity<?> getAllProduct( @PathVariable String keyWord) {
		
		StoreDto storeDto = new StoreDto();

		this.ourStoreService.initStore(storeDto,keyWord);
		
		StoreForm storeForm = this.mapper.map(storeDto, StoreForm.class);

		return new ResponseEntity<>(storeForm,HttpStatus.OK);
	}
	
//	@GetMapping("/findProductFilter/br={brand}/fromPr={fromPr}/toPr={toPr}/filterBy={filterBy}/showMore={showMore}")
//	public ResponseEntity<?> filterProduct(@PathVariable String brand, @PathVariable String fromPr, @PathVariable String toPr, @PathVariable String filterBy, @PathVariable Boolean showMore ) {
//		
//		StoreDto storeDto = new StoreDto();
//		
//		storeDto = this.ourStoreService.setFilterCondition(storeDto, brand, fromPr, toPr, filterBy, showMore);
//		
//		this.ourStoreService.filterProduct(storeDto);
//
//		StoreForm storeForm = this.mapper.map(storeDto, StoreForm.class);
//
//		return new ResponseEntity<>(storeForm,HttpStatus.OK);
//	}

	@PostMapping("/filterProductAPIv1")
	public ResponseEntity<?> filterProductApiv1(@RequestBody StoreForm storeForm){
		
		StoreDto storeDto = new StoreDto();
		
		storeDto = this.mapper.map(storeForm, StoreDto.class);
		
		this.ourStoreService.filterProductv2(storeDto);
		
		storeForm = this.mapper.map(storeDto, StoreForm.class);
		
		return new ResponseEntity<>(storeForm,HttpStatus.OK);
	}
	
}
