package com.yeeshop.yeeserver.app.form;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;

import lombok.Data;

@Data
public class InfoForm {

	private String fullNm;
	
	private String email;
	
	private String phone;
	
	private String userBirth;
	
	private String gender;
	
	private String userAvatar;
	
	private List<AddressProvinceDto> provinces;
	
	private List<AddressDistrictDto> districts;
	
	private List<AddressWardDto> wards;
	
	private String provinceCd;

	private String districtCd;
	
	private String wardCd;

	private String hamlet;
	
	private Boolean initState = false;
}
