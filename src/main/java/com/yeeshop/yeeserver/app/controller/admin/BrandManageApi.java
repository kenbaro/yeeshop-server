package com.yeeshop.yeeserver.app.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.IBrandService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
@RequestMapping("/admin")
public class BrandManageApi {

	@Autowired
	private IBrandService brandService;
	
	@GetMapping("/get-all-brands")
	public ResponseEntity<?> getAllBrand() {

		List<Brand> brands = this.brandService.getAllBrands();
		
		return new ResponseEntity<>(brands, HttpStatus.OK);
	}
	
	@GetMapping("/get-brand-by-id")
	public ResponseEntity<?> getBrandById(HttpServletRequest request) {

		Brand brand = this.brandService.getBrandById(request);
		
		if (brand.getBrandCd() == null) {
			
			return new ResponseEntity<>("Thương hiệu không tồn tại, vui lòng thử lại", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(brand, HttpStatus.OK);
	}
	
	@PostMapping("/update-brand")
	public ResponseEntity<?> updateBrand(@RequestBody Brand brand) {

		YeeMessage yeeMessage = new YeeMessage();
		Boolean updateOk = this.brandService.updateBrand(brand,yeeMessage);
		
		if (!updateOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
	@PostMapping("/add-brand")
	public ResponseEntity<?> addBrand(@RequestBody Brand brand) {

		YeeMessage yeeMessage = new YeeMessage();
		Boolean addOK = this.brandService.addBrand(brand,yeeMessage);
		
		if (!addOK) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
	@GetMapping("/delete-brand")
	public ResponseEntity<?> deleteBrand(HttpServletRequest request) {

		YeeMessage yeeMessage = new YeeMessage();
		Boolean deleteOK = this.brandService.deleteBrand(request,yeeMessage);
		
		if (!deleteOK) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
}
