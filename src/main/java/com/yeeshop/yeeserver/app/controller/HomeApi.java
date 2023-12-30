package com.yeeshop.yeeserver.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;		
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.home.HomeForm;
import com.yeeshop.yeeserver.domain.dto.home.HomeDto;
import com.yeeshop.yeeserver.domain.service.IHomeService;

import lombok.RequiredArgsConstructor;

/**
 * A Controller Class for control Home Api.
 * @author Thai Duy Bao.
 *
 * since 2023
 */
@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class HomeApi {

	/** Performs object mapping. */
	@Autowired 
	private ModelMapper mapper;

	/** Interface for Home Service. */
	@Autowired 
	private IHomeService homeService;

	@GetMapping("/home")
	public ResponseEntity<?> editCommonInfo() {
		
		HomeDto homeDto = new HomeDto();
		
		HomeForm homeForm = new HomeForm();
		
		this.homeService.editInitDto(homeDto);
		
		this.homeService.getUserNm(homeDto);
		
		this.homeService.editCommon(homeDto);

		this.mapper.map(homeDto, homeForm);

		return new ResponseEntity<>(homeForm,HttpStatus.OK);
	}
}
