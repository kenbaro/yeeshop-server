package com.yeeshop.yeeserver.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.IBrandRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.service.IBrandService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class BrandServiceImpl implements IBrandService{

	@Autowired IBrandRepository brandRepository;
	
	@Autowired IUserService userService;
	
	@Autowired IProductRepository productRepository;

	@Override
	public List<Brand> getAllBrands() {
		
		return this.brandRepository.findAll();
	}

	@Override
	public Brand getBrandById(HttpServletRequest request) {

		String brandCd = request.getParameter("brandCd");
		
		Optional<Brand> brOptional = this.brandRepository.findById(brandCd);
		
		Brand brand = brOptional.isPresent() ? brOptional.get() : new Brand();

		return brand;
	}

	@Override
	public Boolean updateBrand(Brand brand, YeeMessage yeeMessage) {
		
		if (YeeStringUtils.isAnyEmpty(brand.getBrandNm(), brand.getBrandCd(),brand.getBrMailContact(), brand.getBrTelContact(), brand.getImgLink())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		Optional<Brand> brOptional = this.brandRepository.findById(brand.getBrandCd());
		
		Brand brand2 = brOptional.isPresent() ? brOptional.get() : null;
		
		if (null == brand2) {

			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		
		brand2.setBrandNm(brand.getBrandNm());
		brand2.setBrMailContact(brand.getBrMailContact());
		brand2.setBrTelContact(brand.getBrTelContact());
		brand2.setImgLink(brand.getImgLink());
		
		String currDateTime= YeeDateTimeUtils.getCurrentDateTime();
		brand2.setUpdatedTime(currDateTime);
		
		User user = this.userService.validateUser();
		
		brand2.setUpdatedBy(user.getEmail());
		
		brand2 = this.brandRepository.save(brand2);
		
		if (null == brand2) {

			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		
		yeeMessage.setMessageTitle("Cập nhật thông tin thương hiệu thành công");
		return true;
	}

	@Override
	public Boolean addBrand(Brand brand, YeeMessage yeeMessage) {

		if (YeeStringUtils.isAnyEmpty(brand.getBrandNm(),brand.getBrMailContact(), brand.getBrTelContact(), brand.getImgLink())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}

		List<Brand> brands = this.brandRepository.findByBrandNm(brand.getBrandNm());
		
		if (brands.size() > 0) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		
		Brand brand2 = new Brand();
		
		brand2.setBrandCd(YeeStringUtils.upperCase(brand.getBrandNm())+ "01");
		brand2.setBrandNm(brand.getBrandNm());
		brand2.setBrMailContact(brand.getBrMailContact());
		brand2.setBrTelContact(brand.getBrTelContact());
		brand2.setImgLink(brand.getImgLink());
		
		String currDateTime= YeeDateTimeUtils.getCurrentDateTime();
		brand2.setUpdatedTime(currDateTime);
		brand2.setCreatedTime(currDateTime);
		User user = this.userService.validateUser();
		
		brand2.setUpdatedBy(user.getEmail());
		
		brand2 = this.brandRepository.save(brand2);
		
		if (null == brand2) {

			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		
		yeeMessage.setMessageTitle("Tạo thương hiệu thành công");
		return true;
	}

	@Override
	public Boolean deleteBrand(HttpServletRequest request, YeeMessage yeeMessage) {

		String id = request.getParameter("brandCd");

		Optional<Brand> brOptional = this.brandRepository.findById(id);
		
		Brand brand = brOptional.isPresent() ? brOptional.get() : null;
		
		if (null == brand) {

			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		
		List<Product> products = this.productRepository.findByBrand(brand);
		
		products.stream().forEach(product -> {
			product.setBrand(null);
		});
		
		products = this.productRepository.saveAll(products);
		this.brandRepository.delete(brand);
		
		yeeMessage.setMessageTitle("Xoá thương hiệu thành công");
		return true;
	}

}
