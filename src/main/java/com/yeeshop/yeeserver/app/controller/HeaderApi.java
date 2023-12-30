package com.yeeshop.yeeserver.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.domain.dto.common.YeeCommonDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeHeaderDto;
import com.yeeshop.yeeserver.domain.service.ICommonService;

import lombok.RequiredArgsConstructor;

/**
 * Api for control Header.
 * @author Thai Duy Bao.
 *
 * since 2023
 */
@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class HeaderApi {

	/** Performs object mapping. */
	@Autowired 
	private ModelMapper mapper;

	@Autowired
	private ICommonService commonService;

	@GetMapping("/headerApi")
	public ResponseEntity<?> index() {
		
		YeeHeaderDto yeeHeaderDto = new YeeHeaderDto();

		this.commonService.getHeader(yeeHeaderDto);
		
		YeeCommonDto yeeCommonDto = this.commonService.editCommon(yeeHeaderDto);

		this.mapper.map(yeeCommonDto, yeeHeaderDto);

		return new ResponseEntity<>(yeeHeaderDto,HttpStatus.OK);
	}
}
