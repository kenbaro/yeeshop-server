package com.yeeshop.yeeserver.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;
import com.yeeshop.yeeserver.domain.service.IAddressApiService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AddressApi {

	@Autowired
	private IAddressApiService addressApiService;
	
	@GetMapping("/get-province-list")
	public ResponseEntity<?> getProvinces() {
		
		List<AddressProvinceDto> provinces = this.addressApiService.getProvinces();
		List<AddressDistrictDto> districts = this.addressApiService.getDistricts(provinces.get(0).getProvinceID().toString());
		List<AddressWardDto> wards = this.addressApiService.getWards(districts.get(0).getDistrictID().toString());
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("provinces", provinces);
		map.put("districts", districts);
		map.put("wards", wards);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@GetMapping("/get-district-list")
	public ResponseEntity<?> getDistricts(HttpServletRequest request) {
		
		String provinceCd = request.getParameter("province_id");
		
		if (null == provinceCd || YeeStringUtils.isEmpty(provinceCd)) {
			return new ResponseEntity<>("Lỗi!!!",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<AddressDistrictDto> districts = this.addressApiService.getDistricts(provinceCd);
		List<AddressWardDto> wards = this.addressApiService.getWards(districts.get(0).getDistrictID().toString());
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("districts", districts);
		map.put("wards", wards);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@GetMapping("/get-ward-list")
	public ResponseEntity<?> getWards(HttpServletRequest request) {
		
		String districtCd = request.getParameter("district_id");
		
		if (null == districtCd || YeeStringUtils.isEmpty(districtCd)) {
			return new ResponseEntity<>("Lỗi!!!",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<AddressWardDto> wards = this.addressApiService.getWards(districtCd);
		HashMap<String, Object> map = new HashMap<>();
		map.put("wards", wards);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@GetMapping("/get-address")
	public ResponseEntity<?> getAddress(HttpServletRequest request) {
		
		String provinceCd = request.getParameter("province_id");
		String districtCd = request.getParameter("district_id");

		HashMap<String, Object> map = new HashMap<>();
		List<AddressDistrictDto> districts = new ArrayList<>();
		List<AddressWardDto> wards = new ArrayList<>();
		List<AddressProvinceDto> provinces = this.addressApiService.getProvinces();
		map.put("provinces", provinces);
		if (null != provinceCd && YeeStringUtils.isNotEmpty(provinceCd)) {
			
			districts = this.addressApiService.getDistricts(provinceCd);
		} else {
			
			
			districts = this.addressApiService.getDistricts(provinces.get(0).getProvinceID().toString());
			
		}
		if (null != districtCd && YeeStringUtils.isNotEmpty(districtCd)) {
			wards = this.addressApiService.getWards(districtCd);
		} else {
			wards = this.addressApiService.getWards(districts.get(0).getDistrictID().toString());
		}
		map.put("districts", districts);
		map.put("wards", wards);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
}
