package com.yeeshop.yeeserver.domain.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_0;
import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_1;
import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.SALE.SALE_OFF;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeBannerType;
import com.yeeshop.yeeserver.domain.dto.common.ProductCartDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeHeaderDto;
import com.yeeshop.yeeserver.domain.dto.home.HomeComponentDto;
import com.yeeshop.yeeserver.domain.dto.home.HomeDto;
import com.yeeshop.yeeserver.domain.dto.home.HomePrdSeriesDto;
import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;
import com.yeeshop.yeeserver.domain.entity.SubCategory;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.repository.IBrandRepository;
import com.yeeshop.yeeserver.domain.repository.IHomeRepository;
import com.yeeshop.yeeserver.domain.repository.IProductSeriesRepository;
import com.yeeshop.yeeserver.domain.repository.ISubCategoryRepository;
import com.yeeshop.yeeserver.domain.service.ICommonService;
import com.yeeshop.yeeserver.domain.service.IHomeService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

/**
 * Service Class For Home Service.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Service
public class HomeServiceImpl implements IHomeService{

	@Autowired
	private IUserService userService;
	
	@Autowired IBrandRepository brandRepository;
	
	@Autowired ICommonService commonService;

	@Autowired IProductSeriesRepository productSeriesRepository;
	
	@Autowired ISubCategoryRepository subCategoryRepository;
	
	@Autowired IHomeRepository homeRepository;

	/** method : get UserNm of customer
	 * @param homeDto : Data Transfer Object for Home API.
	 * */
	@Override
	public void getUserNm(final HomeDto homeDto) {
		
		// get validated user
		User user = this.userService.validateUser();
		
		// header dto
		YeeHeaderDto yeeHeaderDto = new YeeHeaderDto();

		if (null != user) {

			if (!YeeStringUtils.isEmpty(user.getUserNm())) {
				
				yeeHeaderDto.setFullNm(user.getUserNm());
			}		
		}
		
		// set header DTO to objDTO
		homeDto.setYeeHeaderDto(yeeHeaderDto);
	}

	/** method : edit Common for Home API
	 * @param homeDto : Data Transfer Object for Home API.
	 * */
	@Override
	public void editCommon(final HomeDto homeDto) {

		// set banner to Dto
		// get Banner from data base.
		List<Banner> bnList = this.commonService.getListBanners();
		
		for (Banner bn : bnList) {
			
			if (INT_1 == bn.getShowFlg()) {
				
				if (YeeStringUtils.equals(YeeBannerType.BANNER_LARGE, bn.getBannerType().getBnTypeCd())) {
					
					// set banner to DTO
					homeDto.getLargeBanners().add(bn);

				} else if (YeeStringUtils.equals(YeeBannerType.BANNER_SMALL_R, bn.getBannerType().getBnTypeCd())) {
					
					// set banner to DTO 
					homeDto.getSmallRightBanners().add(bn);
				} else if (YeeStringUtils.equals(YeeBannerType.BANNER_HEADER, bn.getBannerType().getBnTypeCd())){
					
					// set banner to headerDto 
					homeDto.getYeeHeaderDto().setHeaderBanner(bn);
				} else if (YeeStringUtils.equals(YeeBannerType.BANNER_LONG, bn.getBannerType().getBnTypeCd())){
					
					// set banner to DTO 
					homeDto.getLongBanners().add(bn);
				} else if (YeeStringUtils.equals(YeeBannerType.BANNER_HALF_LONG, bn.getBannerType().getBnTypeCd())){
					
					// set banner to DTO 
					homeDto.getHalfLongBanners().add(bn);
				}
			}
			
		}
		
		// set Brand List 
		List<Brand> brands = this.brandRepository.findAll();
		
		if (INT_0 < brands.size()) {
			
			homeDto.setBrands(brands);
		}
		
		if (null == homeDto.getHomeComponentDtos()) {
			
			homeDto.setHomeComponentDtos(new ArrayList<>());
		}
		
		// set title for List Component
		List<SubCategory> subCategories = this.subCategoryRepository.findAll();
		List<ProductSeries> productSeries = new ArrayList<>();
		List<String> paramMap = new ArrayList<>();
		if (INT_0 <= subCategories.size()) {
			
			for (SubCategory subCate : subCategories) {
				
				paramMap.clear();

				HomeComponentDto homeComponentDto = new HomeComponentDto();

				homeComponentDto.setComponentTitle(subCate.getSubCateNm());
				homeComponentDto.setComponentCd(subCate.getSubCateCd());
				
				productSeries = this.homeRepository.getProductSeriesBySubCateId(subCate.getSubCateCd());

				for (ProductSeries dto : productSeries) {
					
					HomePrdSeriesDto prDto = new HomePrdSeriesDto();
					
					prDto.setProductSerieCd(dto.getPSeriesCd());
					prDto.setProductSerieNm(dto.getPSeriesNm());
					
					paramMap.add(dto.getPSeriesCd());
					
					if (null == homeComponentDto.getHomeProductSeries()) {
						
						homeComponentDto.setHomeProductSeries(new ArrayList<>());
					}
					
					homeComponentDto.getHomeProductSeries().add(prDto);

				}
				
				List<Product> products = this.homeRepository.getProductsByPSeriesIds(paramMap);

				for (Product product : products) {
					
					ProductCartDto productDto = new ProductCartDto();

					String currentDate= YeeDateTimeUtils.getCurrentDate();
					if (product.getIsAvailabled() == 1
							&& product.getProductSeries() != null 
							&& YeeDateTimeUtils.calcDaysBetween2Dates(product.getDateStart(),currentDate) >= 0) {
						
						// set SKU
						productDto.setProductCd(product.getSKU());
						
						// set Name
						productDto.setProductNm(product.getProductNm());
						
						// set Image
						productDto.setProductImg(product.getProductImage().getMainImage());
						
						// set Unit Price
						productDto.setProductUnitPrice(product.getUnitPrice());
						
						// set Description
						productDto.setProductDescription(product.getProductDescription());
						
						// set Brand Code
						productDto.setBrandCd(product.getBrand().getBrandCd());
						
						// set Brand Name
						productDto.setBrandNm(product.getBrand().getBrandNm());
						
						// set Price after sale
						// case saleType is SALE OFF
						if (product.getSale() != null) {
							
							if (SALE_OFF == product.getSale().getSaleType()) {
								
								productDto.setProductDiscount(product.getSale().getDisCount());
								
								BigDecimal afterPrice = YeeDecimalUtils.calcPriceDiscount(productDto.getProductDiscount(), productDto.getProductUnitPrice());
								
								productDto.setProductDiscountPrice(YeeDecimalUtils.formatDecimalWithComma(afterPrice));
							}
						}
						
						
						
					}
					
					if (null == homeComponentDto.getProducts()) {
						
						homeComponentDto.setProducts(new ArrayList<>());
					}
					
					if (YeeStringUtils.isNotEmpty(productDto.getProductCd())) {
						
						homeComponentDto.getProducts().add(productDto);
					}
					
				}
				
				homeDto.getHomeComponentDtos().add(homeComponentDto);
			}
		}
	}
	
	
	/** method : edit Initialize for Home API
	 * @param homeDto : Data Transfer Object for Home API.
	 * */
	@Override
	public void editInitDto(final HomeDto homeDto) {
		
//		if(null == homeDto.getYeeFooterDto()) {
//			
//			homeDto.setYeeFooterDto(new YeeFooterDto());
//		}
		
		if (null == homeDto.getLargeBanners()) {
			
			homeDto.setLargeBanners(new ArrayList<>());
		}
		
		if (null == homeDto.getSmallRightBanners()) {
			
			homeDto.setSmallRightBanners(new ArrayList<>());
		}
		
		if (null == homeDto.getLongBanners()) {
			
			homeDto.setLongBanners(new ArrayList<>());
		}
		
		if (null == homeDto.getHalfLongBanners()) {
			
			homeDto.setHalfLongBanners(new ArrayList<>());
		}
		
		if (null == homeDto.getHomeComponentDtos()) {
			
			homeDto.setHomeComponentDtos(new ArrayList<>());
		}

	}
	
}
