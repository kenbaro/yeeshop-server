package com.yeeshop.yeeserver.domain.dto.auth;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;

import lombok.Data;

@Data
public class InfoDto {

	private String userId;

	private String fullNm;
	
	private String email;
	
	private String passWord;
	
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
	
	private String userStatus;
	
	private String role;
	
	private String typeUser;
	
	private String updatedBy;
	
	private String updatedTime;
	
	private String createdBy;
	
	
	private String createdTime;
	
}
