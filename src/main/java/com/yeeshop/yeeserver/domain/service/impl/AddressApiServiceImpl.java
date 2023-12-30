package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;
import com.yeeshop.yeeserver.domain.dto.checkout.CheckoutDto;
import com.yeeshop.yeeserver.domain.model.district.DistrictGHN;
import com.yeeshop.yeeserver.domain.model.district.DistrictGHNData;
import com.yeeshop.yeeserver.domain.model.province.ProvinccGHNData;
import com.yeeshop.yeeserver.domain.model.province.ProvinceGHN;
import com.yeeshop.yeeserver.domain.model.ward.WardGHN;
import com.yeeshop.yeeserver.domain.model.ward.WardGHNData;
import com.yeeshop.yeeserver.domain.service.IAddressApiService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

@Service
public class AddressApiServiceImpl implements IAddressApiService {

	private final String URL_API = "https://dev-online-gateway.ghn.vn/shiip/public-api";
	
	private final String TOKEN = "212110cc-806c-11ee-8bfa-8a2dda8ec551";

	private void setProvince2Dto(final ProvinccGHNData data,List<AddressProvinceDto> provinces) {
		
		AddressProvinceDto provinceDto = new AddressProvinceDto();

		provinceDto.setCountryID(data.getCountryID());
		provinceDto.setProvinceID(data.getProvinceID());
		provinceDto.setProvinceName(data.getProvinceName());
		provinceDto.setCode(data.getCode());
		provinces.add(provinceDto);
	}
	
	@Override
	public List<AddressProvinceDto> getProvinces() {
		
		List<AddressProvinceDto> provinces = new ArrayList<>();
		
		String urlApi = this.URL_API + "/master-data/province";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", this.TOKEN);
		
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		ResponseEntity<String> res = restTemplate.exchange(urlApi, HttpMethod.POST, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {

			// get response data
			String json = res.getBody();
			
			Gson gson = new Gson();
			
			ProvinceGHN province = gson.fromJson(json, ProvinceGHN.class);
			
			province.getData().stream().forEach(e -> setProvince2Dto(e, provinces));
			
			
		}

		return provinces;
	}

	private void setDistrict2Dto(final DistrictGHNData data, List<AddressDistrictDto> districts) {
		
		if (!YeeStringUtils.isNumbericChk(data.getDistrictName())) {
			
			AddressDistrictDto districtDto = new AddressDistrictDto();
			if (data.getType() != 2 || data.getCode() != null) {
				
				districtDto.setDistrictID(data.getDistrictID());
				
				districtDto.setDistrictName(data.getDistrictName());
				
				districtDto.setProvinceID(data.getProvinceID());
				
				districtDto.setCode(data.getCode());
				
				districtDto.setType(data.getType());
				
				districtDto.setSupportType(data.getSupportType());
				districts.add(districtDto);
			}
		}
	}
	
	@Override
	public List<AddressDistrictDto> getDistricts(String provinceId) {
		
		List<AddressDistrictDto> districts = new ArrayList<>();
		
		String urlApi = this.URL_API + "/master-data/district";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", this.TOKEN);
		
		headers.set("Content-Type", "application/json");
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlApi)
		        .queryParam("province_id", provinceId);
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		
		ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {
			
			// get response data
			String json = res.getBody();
			
			Gson gson = new Gson();
			
			DistrictGHN district = gson.fromJson(json, DistrictGHN.class);
		
			district.getData().stream().forEach(e -> setDistrict2Dto(e, districts));
			
		} 
		
		return districts;
	}

	private void setWard2Dto(final WardGHNData data, List<AddressWardDto> wards) {
		
		AddressWardDto dto = new AddressWardDto();
		
		dto.setWardId(data.getWardCode());
		dto.setWardNm(data.getWardName());
		dto.setDistrictId(data.getDistrictID());
		
		wards.add(dto);
	}
	
	@Override
	public List<AddressWardDto> getWards(String districtCd) {
		
		List<AddressWardDto> wards = new ArrayList<>();
		
		String apiUrl = this.URL_API + "/master-data/ward";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", this.TOKEN);

		headers.set("Content-Type", "application/json");

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
		        .queryParam("district_id", districtCd);
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		
		ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {
			
			// get response data
			String json = res.getBody();
			
			Gson gson = new Gson();
			
			WardGHN ward = gson.fromJson(json, WardGHN.class);
			

			ward.getData().stream().forEach(e -> setWard2Dto(e, wards));
		}
		
		return wards;
	}

}
