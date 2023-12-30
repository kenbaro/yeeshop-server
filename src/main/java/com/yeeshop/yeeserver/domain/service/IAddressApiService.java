package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;

public interface IAddressApiService {

	public List<AddressProvinceDto> getProvinces();
	
	public List<AddressDistrictDto> getDistricts(String provinceCd);
	
	public List<AddressWardDto> getWards(String districtCd);
}
