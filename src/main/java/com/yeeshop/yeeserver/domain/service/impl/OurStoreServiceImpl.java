package com.yeeshop.yeeserver.domain.service.impl;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.SALE.SALE_OFF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.dto.common.ProductCartDto;
import com.yeeshop.yeeserver.domain.dto.ourstore.StoreBrandDto;
import com.yeeshop.yeeserver.domain.dto.ourstore.StoreDto;
import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.repository.IBrandRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.service.IOurStoreService;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

@Service
public class OurStoreServiceImpl implements IOurStoreService{

	@Autowired
	private IBrandRepository brandRepository;
	
	@Autowired IProductRepository productRepository;
	
	@Override
	public void initStore(final StoreDto storeDto, String keyWord) {
		
		this.filterNoneCondition(storeDto,keyWord);
		
	}
	
	private void filterNoneCondition(final StoreDto storeDto, String keyWord) {
		
		this.editDTO(storeDto);

		List<Brand> brands = brandRepository.findAll();
		
		for (Brand brand : brands) {
			
			StoreBrandDto storeCateDto = new StoreBrandDto();
			
			storeCateDto.setBrandCd(brand.getBrandCd());
			
			storeCateDto.setBrandNm(brand.getBrandNm());
			
			storeCateDto.setIsActive(false);
			
			storeDto.getBrands().add(storeCateDto);
		}

		List<Product> products = new ArrayList<>();
		 
		if (YeeStringUtils.isNotEmpty(keyWord)) {
			 
			if(!YeeStringUtils.chkSpecialCharacter(keyWord)) {
				 
				products = this.productRepository.findByProductNmContaining(keyWord);
			}
		} else {
			 
			products = this.productRepository.findAvailableProducts();
			 
		}
		 
		Integer productCnt = products.size();
		if (!storeDto.getShowMore()) {
			
			products = products.stream().limit(8).toList();
		}
		
		if (YeeStringUtils.isNotEmpty(storeDto.getFilterFromPrice())
				&& YeeStringUtils.isNotEmpty(storeDto.getFilterToPrice())) {
			
			
		}
		
		if (YeeStringUtils.isNotEmpty(storeDto.getFilterBy()) 
				&& YeeStringUtils.isNumbericChk(storeDto.getFilterBy())) {
			
			if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_1) {
				
				products = products.stream()
						.sorted(Comparator.comparingInt(Product::getSoldQty).reversed())
						.collect(Collectors.toList());
			} else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_2) {
				
				products = products.stream()
						.sorted(Comparator.comparing(Product::getProductNm, String::compareTo))
						.collect(Collectors.toList());
			}
			else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_3) {
				
				products = products.stream()
						.sorted(Comparator.comparing(Product::getProductNm, String::compareTo).reversed())
						.collect(Collectors.toList());
			}
			else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_4) {
				
				products = products.stream()
						.sorted((p1,p2) -> YeeDecimalUtils.calcPriceDiscount(p1.getSale().getDisCount(), p1.getUnitPrice())
								.compareTo(YeeDecimalUtils.calcPriceDiscount(p2.getSale().getDisCount(), p2.getUnitPrice())))
						.collect(Collectors.toList());
			} else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_5) {

				products = products.stream()
						.sorted((p1,p2) -> YeeDecimalUtils.calcPriceDiscount(p2.getSale().getDisCount(), p2.getUnitPrice())
								.compareTo(YeeDecimalUtils.calcPriceDiscount(p1.getSale().getDisCount(), p1.getUnitPrice())))
						.collect(Collectors.toList());
			}
		}
		
		

		this.showProducts(storeDto, products, productCnt);
	}
	
	private void editDTO(final StoreDto storeDto) {
		
		if (null == storeDto.getBrands()) {
			
			storeDto.setBrands(new ArrayList<>());
		}
		
		if (null == storeDto.getProducts()) {
			
			storeDto.setProducts(new ArrayList<>());
		}
		
	}

	private void setEntitytoDto(final StoreDto storeDto, final List<Product> products ) {
		
		storeDto.setProducts(new ArrayList<>());
		for (Product product : products) {
			
			ProductCartDto productDto = new ProductCartDto();

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
			if (null != product.getSale()) {
				
				if (SALE_OFF == product.getSale().getSaleType()) {
					
					productDto.setProductDiscount(product.getSale().getDisCount());
					
					BigDecimal afterPrice = YeeDecimalUtils.calcPriceDiscount(productDto.getProductDiscount(), productDto.getProductUnitPrice());
					
					productDto.setProductDiscountPrice(YeeDecimalUtils.formatDecimalWithComma(afterPrice));
				}
			}

			storeDto.getProducts().add(productDto);
		}
	}
	
	private void showProducts(final StoreDto storeDto, List<Product> products, final Integer productCnt) {
		
		if (0 <= productCnt) {
			
			if (8 < productCnt && !storeDto.getShowMore()) {
				
				storeDto.setProductShowQty(8);
				storeDto.setProductRemainingQty(productCnt - 8);
				 products = products.stream().limit(8).toList();
			} else {
				
				storeDto.setProductShowQty(productCnt);
				storeDto.setProductRemainingQty(0);
			}
			
			this.setEntitytoDto(storeDto,products);
			
			//storeDto.setShowMore(false);
		}
	}
	
//	@Override
//	public void filterProduct(StoreDto storeDto) {
//		if (YeeStringUtils.isEmpty(storeDto.getFilterBrand()) 
//				&& YeeStringUtils.isEmpty(storeDto.getFilterFromPrice())
//				&& YeeStringUtils.isEmpty(storeDto.getFilterToPrice())) {
//
//			this.filterNoneCondition(storeDto);
//		} else {
//			this.editDTO(storeDto);
//
//			storeDto.setBrands(new ArrayList<>());
//			List<Brand> brands = brandRepository.findAll();
//			
//			for (Brand brand : brands) {
//				
//				StoreBrandDto storeCateDto = new StoreBrandDto();
//				
//				storeCateDto.setBrandCd(brand.getBrandCd());
//				
//				storeCateDto.setBrandNm(brand.getBrandNm());
//				
//				storeCateDto.setIsActive(false);
//				
//				storeDto.getBrands().add(storeCateDto);
//			}
//			
//			List<Product> products = new ArrayList<>();
//
//			if (!YeeStringUtils.isEmpty(storeDto.getFilterBrand())) {
//				
//				Brand brand = this.brandRepository.findById(storeDto.getFilterBrand()).isPresent() 
//								? this.brandRepository.findById(storeDto.getFilterBrand()).get() 
//								: new Brand();
//				products = this.productRepository.findByBrand(brand);
//
//			}
//			
//				
//			if (YeeStringUtils.isNotEmpty(storeDto.getFilterFromPrice()) 
//					&& YeeStringUtils.isNotEmpty(storeDto.getFilterToPrice())) {
//				
//				//products.
//			}
//				
//			if (YeeStringUtils.isNotEmpty(storeDto.getFilterBy()) 
//					&& YeeStringUtils.isNumbericChk(storeDto.getFilterBy())) {
//				
//				if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_1) {
//					
//					products = products.stream()
//							.sorted(Comparator.comparingInt(Product::getSoldQty).reversed())
//							.collect(Collectors.toList());
//				} else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_2) {
//					
//					products = products.stream()
//							.sorted(Comparator.comparing(Product::getProductNm, String::compareTo))
//							.collect(Collectors.toList());
//				}
//				else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_3) {
//					
//					products = products.stream()
//							.sorted(Comparator.comparing(Product::getProductNm, String::compareTo).reversed())
//							.collect(Collectors.toList());
//				}
//				else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_4) {
//					
//					products = products.stream()
//							.sorted((p1,p2) -> YeeDecimalUtils.calcPriceDiscount(p1.getSale().getDisCount(), p1.getUnitPrice())
//									.compareTo(YeeDecimalUtils.calcPriceDiscount(p2.getSale().getDisCount(), p2.getUnitPrice())))
//							.collect(Collectors.toList());
//				} else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_5) {
//
//					products = products.stream()
//							.sorted((p1,p2) -> YeeDecimalUtils.calcPriceDiscount(p2.getSale().getDisCount(), p2.getUnitPrice())
//									.compareTo(YeeDecimalUtils.calcPriceDiscount(p1.getSale().getDisCount(), p1.getUnitPrice())))
//							.collect(Collectors.toList());
//				}
//			}
//			
//			Integer productCnt = products.size();
//
//			if (!storeDto.getShowMore()) {
//				
//				products = products.stream().limit(8).toList();
//			}
//
//			this.setEntitytoDto(storeDto,products);
//			
//			this.showProducts(storeDto, products, productCnt);
//			
//		}
//		
//	}

	@Override
	public StoreDto setFilterCondition(StoreDto storeDto, String br, String fromPr, String toPr, String filterBy,
			Boolean showMore) {

		storeDto.setFilterBrand(br);
		storeDto.setFilterFromPrice(fromPr);
		storeDto.setFilterToPrice(toPr);
		storeDto.setFilterBy(filterBy);
		storeDto.setShowMore(showMore);
		return storeDto;
	}

	@Override
	public void filterProductv2(final StoreDto storeDto) {
		
		List<Product> products = this.productRepository.findAvailableProducts();
		//List<Product> products = new ArrayList<>();
		if (!YeeStringUtils.isEmpty(storeDto.getFilterBrand())) {
			
			Brand brand = this.brandRepository.findById(storeDto.getFilterBrand()).isPresent() 
							? this.brandRepository.findById(storeDto.getFilterBrand()).get() 
							: new Brand();
			products = this.productRepository.findByBrand(brand);
		}
		

		if (YeeStringUtils.isNotEmpty(storeDto.getFilterFromPrice())
				&& YeeStringUtils.isNotEmpty(storeDto.getFilterToPrice())) {
			
			BigDecimal fromPrice = new BigDecimal(YeeStringUtils.removeComma(storeDto.getFilterFromPrice()));
			BigDecimal toPrice = new BigDecimal(YeeStringUtils.removeComma(storeDto.getFilterToPrice()));
			
			products = products.stream().filter((p) -> p.getSale() != null ?
					
					YeeDecimalUtils.calcPriceDiscount(p.getSale().getDisCount(), p.getUnitPrice()).compareTo(fromPrice) >= 0 
					&& YeeDecimalUtils.calcPriceDiscount(p.getSale().getDisCount(), p.getUnitPrice()).compareTo(toPrice) <= 0

					: new BigDecimal(YeeStringUtils.removeComma(p.getUnitPrice())).compareTo(fromPrice) >= 0 
					&& new BigDecimal(YeeStringUtils.removeComma(p.getUnitPrice())).compareTo(toPrice) <= 0
					).collect(Collectors.toList());
		}
		
		if (YeeStringUtils.isNotEmpty(storeDto.getFilterBy()) 
				&& YeeStringUtils.isNumbericChk(storeDto.getFilterBy())) {
			
			if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_1) {
				
				products = products.stream()
						.sorted(Comparator.comparingInt(Product::getSoldQty).reversed())
						.collect(Collectors.toList());
			} else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_2) {
				
				products = products.stream()
						.sorted(Comparator.comparing(Product::getProductNm, String::compareTo))
						.collect(Collectors.toList());
			}
			else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_3) {
				
				products = products.stream()
						.sorted(Comparator.comparing(Product::getProductNm, String::compareTo).reversed())
						.collect(Collectors.toList());
			}
			else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_4) {
				
				products = products.stream()
						.sorted(
							(p1,p2) -> 
								(null != p1.getSale() 
									? YeeDecimalUtils.calcPriceDiscount(p1.getSale().getDisCount(), p1.getUnitPrice())
									: new BigDecimal(YeeStringUtils.removeComma(p1.getUnitPrice())))
								.compareTo(
									(null != p2.getSale()
									? YeeDecimalUtils.calcPriceDiscount(p2.getSale().getDisCount(), p2.getUnitPrice())
									: new BigDecimal(YeeStringUtils.removeComma(p2.getUnitPrice())))
								)
							)
						.collect(Collectors.toList());
			} else if (Integer.valueOf(storeDto.getFilterBy()) == YeeCommonConst.YeeNumber.INT_5) {

				products = products.stream()
						.sorted((p1,p2) -> 
								(null != p2.getSale() 
								? YeeDecimalUtils.calcPriceDiscount(p2.getSale().getDisCount(), p2.getUnitPrice())
								: new BigDecimal(YeeStringUtils.removeComma(p2.getUnitPrice())))
							.compareTo(
								(null != p1.getSale()
								? YeeDecimalUtils.calcPriceDiscount(p1.getSale().getDisCount(), p1.getUnitPrice())
								: new BigDecimal(YeeStringUtils.removeComma(p1.getUnitPrice())))
							)
						)
						.collect(Collectors.toList());
			}
		}

		Integer productCnt = products.size();

		this.showProducts(storeDto, products, productCnt);
		//this.setEntitytoDto(storeDto,products);
	}
}
