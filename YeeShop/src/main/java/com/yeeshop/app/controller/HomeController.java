package com.yeeshop.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.app.common.utils.YeeCollectionUtils;

import com.yeeshop.app.common.utils.YeeStringUtils;

/**
 * 
 * */
@RestController
@RequestMapping("home")
public class HomeController {

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String test() {
		String a= "2022/02/02";
		List<String> list = new ArrayList<>();
		if(!YeeStringUtils.isEmpty(a)) {
			if(YeeCollectionUtils.isEmpty(list)) {
				return "No";
			}
		}
		return "Yes";
	}
	
}
