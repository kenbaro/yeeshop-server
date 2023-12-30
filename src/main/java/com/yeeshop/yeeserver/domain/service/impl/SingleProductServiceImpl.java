package com.yeeshop.yeeserver.domain.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.SALE.SALE_OFF;
import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_0;

import com.yeeshop.yeeserver.domain.admin.dto.productmanage.CategoryDto;
import com.yeeshop.yeeserver.domain.admin.dto.productmanage.EditProductDto;
import com.yeeshop.yeeserver.domain.admin.dto.productmanage.SubEditProductDto;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.dto.common.ProductImageDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.AutoCompleteProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.CateSuggestDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.ProductInfoDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.ProductSuggestDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.RelatedProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductColor;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductImage;
import com.yeeshop.yeeserver.domain.dto.singleproduct.SingleProductStorageDto;
import com.yeeshop.yeeserver.domain.entity.Brand;
import com.yeeshop.yeeserver.domain.entity.Category;
import com.yeeshop.yeeserver.domain.entity.OrderDetails;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.ProductAttribute;
import com.yeeshop.yeeserver.domain.entity.ProductImage;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.entity.SubCategory;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.IBrandRepository;
import com.yeeshop.yeeserver.domain.repository.ICategoryRepository;
import com.yeeshop.yeeserver.domain.repository.IHomeRepository;
import com.yeeshop.yeeserver.domain.repository.IOrderDetailsRepository;
import com.yeeshop.yeeserver.domain.repository.IProductAttributeRepository;
import com.yeeshop.yeeserver.domain.repository.IProductImageRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.repository.IProductSeriesRepository;
import com.yeeshop.yeeserver.domain.repository.ISalesRepository;
import com.yeeshop.yeeserver.domain.repository.ISingleProductRepository;
import com.yeeshop.yeeserver.domain.repository.ISubCategoryRepository;
import com.yeeshop.yeeserver.domain.service.ISingleProductService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class SingleProductServiceImpl implements ISingleProductService{

	@Autowired private IProductRepository productRepository;
	
	@Autowired private ISingleProductRepository singleProductRepository;
	
	@Autowired private IProductAttributeRepository productAttributeRepository;

	@Autowired private IHomeRepository homeRepository;
	
	@Autowired private IProductSeriesRepository productSeriesRepository;
	
	@Autowired private IBrandRepository brandRepository;
	
	@Autowired private ISubCategoryRepository subCategoryRepository;
	
	@Autowired private ICategoryRepository categoryRepository;
	
	@Autowired private ISalesRepository salesRepository;
	
	@Autowired private IProductImageRepository productImageRepository;
	
	@Autowired private IOrderDetailsRepository orderDetailsRepository;
	
	@Autowired private ModelMapper modelMapper;
	
	@Override
	public void initProduct(final SingleProductDto singleProductDto, final String SKU) throws Exception {
		
		Product product = this.productRepository.findActiveProductBySKU(SKU);

		if (null != product) {

			// update view Count when customer click view detail
			product.setProductViewCnt(product.getProductViewCnt() + INT_1);
			this.productRepository.save(product);

			singleProductDto.setSKU(product.getSKU());

			singleProductDto.setProductNm(product.getProductNm());

			singleProductDto.setOldPrice(product.getUnitPrice());

			if (product.getSale() != null) {

				if (INT_1 != product.getSale().getIsExprired()) {

					singleProductDto.setNewPrice(
							YeeDecimalUtils.formatDecimalWithComma(YeeDecimalUtils
									.calcPriceDiscount(product.getSale().getDisCount(), product.getUnitPrice())));
				}
			} else {

				singleProductDto.setNewPrice(singleProductDto.getOldPrice());
			}


			if (INT_0 >= product.getProductQuantity()) {

				singleProductDto.setProductStatus(INT_0);
			} else {

				singleProductDto.setProductStatus(INT_1);
			}

			if (product.getProductSeries() != null) {
				
				singleProductDto.setProductCate(product.getProductSeries().getSubCategory().getSubCateCd());

				singleProductDto.setProductCateNm(product.getProductSeries().getSubCategory().getSubCateNm());
			}
			
			if (product.getSale() != null) {
				
				singleProductDto.setSales(product.getSale());;
			}
				
			if (product.getBrand()!=null) {
				
				singleProductDto.setProductBrand(product.getBrand().getBrandCd());

				singleProductDto.setProductBrandNm(product.getBrand().getBrandNm());
			}
			
			singleProductDto.setProductAttribute(product.getProductAttribute());

			singleProductDto.setProductDesription(product.getProductDescription());

			singleProductDto.setProductSeries(product.getProductSeries());

			singleProductDto.setSoldQty(product.getSoldQty());

			singleProductDto.setOrderQty(String.valueOf(INT_1));

			singleProductDto.setTempPrice(YeeDecimalUtils.yeeMultiply(singleProductDto.getOrderQty(),singleProductDto.getNewPrice()));

			// set image

			SingleProductImage productImage = new SingleProductImage();

			productImage.setMainImage(product.getProductImage().getMainImage());
			productImage.setImage1(product.getProductImage().getImage1());
			productImage.setImage2(product.getProductImage().getImage2());
			productImage.setImage3(product.getProductImage().getImage3());

			singleProductDto.setImage(productImage);

			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", product.getProductSeries().getPSeriesCd());

			// set color list
			List<Product> products = this.singleProductRepository.getProductBySeriesId(paramMap);


			List<SingleProductColor> productColors = new ArrayList<>();
			SingleProductColor productColor = new SingleProductColor();

			List<SingleProductStorageDto> productStorageDtos = new ArrayList<>();
			SingleProductStorageDto productStorageDto = new SingleProductStorageDto();

			Set<String> setColors = new HashSet<>();
			Set<String> setStorages = new HashSet<>();
			for (Product p : products) {

				setStorages.add(p.getProductStorage());
				setColors.add(p.getProductColor());
			}

			List<String> listColorNms = new ArrayList<>(setColors);
			List<String> listStorageNms = new ArrayList<>(setStorages);
			Collections.sort(listColorNms);
			Collections.sort(listStorageNms);
			for(Integer idx = INT_0; idx < listColorNms.size(); idx++) {

				productColor = new SingleProductColor();
				productColor.setColorCd(idx.toString());
				productColor.setColorNm(listColorNms.get(idx));

				if (YeeStringUtils.equals(productColor.getColorNm(), product.getProductColor())) {

					singleProductDto.setProductColor(productColor);
				}

				productColors.add(idx, productColor);
			}

			for(Integer idx = INT_0; idx < listStorageNms.size(); idx++) {

				productStorageDto = new SingleProductStorageDto();


				productStorageDto.setStorageCd(idx.toString());
				productStorageDto.setStorageNm(listStorageNms.get(idx));

				if (YeeStringUtils.equals(productStorageDto.getStorageNm(), product.getProductStorage())) {

					singleProductDto.setProductStorageDto(productStorageDto);
				}

				productStorageDtos.add(productStorageDto);
			}

			singleProductDto.setProductColors(productColors);
			singleProductDto.setProductStorages(productStorageDtos);

			// edit disabled color
			paramMap.put("storage", singleProductDto.getProductStorageDto().getStorageNm());
			List<Product> productList = this.singleProductRepository.getProductBySeriesIdAndStorage(paramMap);

			for(Product p : productList) {

				for (SingleProductColor pColor : singleProductDto.getProductColors()) {

					if (YeeStringUtils.equals(p.getProductColor(), pColor.getColorNm())) {

						pColor.setShowFlg(true);
					}
				}
			}
		}

	}

	@Override
	public void getProduct(final SingleProductDto singleProductDto, final String storageCd, final String colorCd) throws Exception {
		
		Product product = this.getProductByStorageAndColor(singleProductDto,storageCd, colorCd);
		
		if (null != product) {
			
			initProduct(singleProductDto, product.getSKU());
		} 
	}
	
	private Product getProductByStorageAndColor(final SingleProductDto singleProductDto, final String storageCd, final String colorCd) {
		
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("storage", storageCd);
		if (singleProductDto.getProductSeries() != null) {
			paramMap.put("id", singleProductDto.getProductSeries().getPSeriesCd());
		} else {
			
			paramMap.put("id", "");
		}
		
		paramMap.put("color", colorCd);

		Product product  = this.singleProductRepository.getProductByStorageAndColor(paramMap);
		
		if (null != product) {
			
			return product;
		} else {
			
			List<Product> productList = this.singleProductRepository.getProductBySeriesIdAndStorage(paramMap);
			if (0 < productList.size()) {
				
				return productList.get(0);
			}
		}

		return null;
	}

	@Override
	public void getRelatedProduct(final SingleProductDto singleProductDto) {
		
		List<String> paramMap = new ArrayList<>();;
		
		if (singleProductDto.getProductSeries() != null) {
			
			paramMap.add(singleProductDto.getProductSeries().getPSeriesCd());
		} else {
			
			paramMap.add("");
		}

		List<Product> products = this.homeRepository.getProductsByPSeriesIds(paramMap);
		
		if (null == singleProductDto.getRelatedProductDtos()) {
			
			singleProductDto.setRelatedProductDtos(new ArrayList<>());
		}

		if (0 < products.size()) {
			
			for (Product p : products) {
				
				if (!YeeStringUtils.equals(p.getProductNm(), singleProductDto.getProductNm())) {
					
					RelatedProductDto relatedProductDto = new RelatedProductDto();
					
					// set SKU
					relatedProductDto.setProductCd(p.getSKU());
					
					// set Name
					relatedProductDto.setProductNm(p.getProductNm());
					
					// set Image
					relatedProductDto.setProductImg(p.getProductImage().getMainImage());
					
					// set Unit Price
					relatedProductDto.setProductUnitPrice(p.getUnitPrice());
					
					// set Description
					relatedProductDto.setProductDescription(p.getProductDescription());
					
					if (p.getBrand()!=null) {
						
						// set Brand Code
						relatedProductDto.setBrandCd(p.getBrand().getBrandCd());
						
						// set Brand Name
						relatedProductDto.setBrandNm(p.getBrand().getBrandNm());
					}
					
					
					// set Price after sale
					if (p.getSale() != null) {
						
						// case saleType is SALE OFF
						if (SALE_OFF == p.getSale().getSaleType()) {
							
							relatedProductDto.setProductDiscount(p.getSale().getDisCount());
							
							BigDecimal afterPrice = YeeDecimalUtils.calcPriceDiscount(relatedProductDto.getProductDiscount(), relatedProductDto.getProductUnitPrice());
							
							relatedProductDto.setProductDiscountPrice(YeeDecimalUtils.formatDecimalWithComma(afterPrice));
						}
					}
									
					singleProductDto.getRelatedProductDtos().add(relatedProductDto);
				}
			}
		}
		
		
	}

	@Override
	public Boolean validateProduct(final SingleProductDto singleProductDto) {
		
		Integer quantity = INT_0;

		if (! YeeStringUtils.isNumbericChk(singleProductDto.getOrderQty())) {
			
			return false;
		}
		
		quantity = Integer.valueOf(singleProductDto.getOrderQty());
		
		if (INT_0 >= quantity) {
			
			return false;
		}
		
		Product product = this.productRepository.findActiveProductBySKU(singleProductDto.getSKU());
		
		if (null == product) {
			
			return false;
		}

		if (product.getProductQuantity() < quantity) {
			
			return false;
		}
		
		return true;
	}

	@Override
	public Boolean validateKeyWord(String kw) {
		
		return YeeStringUtils.chkSpecialCharacter(kw);
	}

	@Override
	public void initAutoComplete(AutoCompleteProductDto autoCompleteProductDto, String kw) {
		
		if (!YeeStringUtils.chkSpecialCharacter(kw) && !YeeStringUtils.isEmpty(kw)) {
			
			autoCompleteProductDto.setCateSuggests(new ArrayList<>());
			autoCompleteProductDto.setProductSuggests(new ArrayList<>());
			
			List<ProductSeries> productSeries = this.productSeriesRepository.findBypSeriesNmContaining(kw);
			
			if (0 < productSeries.size()) {
				
				productSeries.stream().forEach(ps -> {
					
					CateSuggestDto cateSuggestDto = new CateSuggestDto();
					cateSuggestDto.setCateId(ps.getPSeriesCd());
					cateSuggestDto.setCateNm(ps.getPSeriesNm());

					autoCompleteProductDto.getCateSuggests().add(cateSuggestDto);
				});
			}
			
			
			
			List<Product> products = this.productRepository.findByProductNmContaining(kw);
			
			if (0 < products.size()) {
				
				products.stream().forEach(p -> {
					
					if (1 == p.getIsAvailabled()) {

						ProductSuggestDto productSuggestDto = new ProductSuggestDto();
						productSuggestDto.setSKU(p.getSKU());
						productSuggestDto.setProductNm(p.getProductNm());
						productSuggestDto.setProductImage(p.getProductImage().getMainImage());
						productSuggestDto.setProductPrice(p.getUnitPrice());
						
						autoCompleteProductDto.getProductSuggests().add(productSuggestDto);
					}
					
				});
			}
			
		}
		
	}

	@Override
	public List<ProductInfoDto> getAllProduct(HttpServletRequest request) {
		
		List<ProductInfoDto> listProductsDto = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		if (null != request) {
			
			String type = request.getParameter("typeSearch");
			
			if (YeeStringUtils.equals(type, "1")) {
				
				String kw = request.getParameter("keyword");
				
				products = this.productRepository.findByProductNmContaining(kw);
			} else {
				
				String kw = request.getParameter("keyword");
				Optional<Product> product = this.productRepository.findById(kw);
				
				if (product.isPresent()) {
					
					products.add(product.get());
				} 
			}
		} else {
			
			products = this.productRepository.findAll();
		}
		
		if (products.size() < 1) {
			
			return listProductsDto;
		}
		
		//products.stream().collect(Collectors.groupingBy(Product::getProductNm));

		Map<String, Long> nameCountMap =  products.stream().collect(Collectors.groupingBy(Product::getProductNm,Collectors.counting()));
		List<Product> filteredList = products.stream()
                .collect(Collectors.toMap(
                        Product::getProductNm,
                        Function.identity(),
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
		Collections.sort(filteredList, (obj1, obj2) -> obj1.getProductNm().compareTo(obj2.getProductNm()));
		
		filteredList.stream().forEach(product -> {
			
			ProductInfoDto singleProductDto = new ProductInfoDto();
			
			singleProductDto.setSKU(product.getSKU());
			singleProductDto.setProductNm(product.getProductNm());
			singleProductDto.setOtherProductLength(String.valueOf(nameCountMap.get(product.getProductNm())-1));
			SingleProductImage singleProductImage = new SingleProductImage();
			singleProductImage.setMainImage(product.getProductImage().getMainImage());
			singleProductDto.setImage(singleProductImage);
			singleProductDto.setColor(product.getProductColor());
			singleProductDto.setSoldQty(product.getSoldQty());
			singleProductDto.setStockQuantity(product.getProductQuantity());
			
			if (product.getProductSeries() != null) {
				
				singleProductDto.setProductCateNm(product.getProductSeries().getPSeriesNm());
			} else {
				
				singleProductDto.setProductCateNm("Đang cập nhật");
			}
			
			if (product.getBrand() != null) {
				singleProductDto.setProductBrandNm(product.getBrand().getBrandNm());
			} else {
				singleProductDto.setProductBrandNm("Đang cập nhật");
			}
			singleProductDto.setProductStatus(product.getIsAvailabled());
			if (null != product.getSale()) {
				
				
				singleProductDto.setSales(product.getSale());
				String saleNm = YeeStringUtils.EMPTY;
				if (product.getSale().getIsExprired() == 0) {
					
					saleNm = product.getSale().getSaleNm();
				} else {
					
					saleNm = product.getSale().getSaleNm() + "(Đã hết hạn)";
				}
				
				singleProductDto.setSaleTitle(saleNm);
				
			} else {
				
				singleProductDto.setSaleTitle("Không có");
			}
			listProductsDto.add(singleProductDto);
		});

		return listProductsDto;
	}

	@Override
	public Boolean editActiveProductByAdmin(String sku, YeeMessage message, Boolean isActive) {

		String phrase = isActive ? "kích hoạt" : "vô hiệu hoá";
		if (YeeStringUtils.isEmpty(sku) || YeeStringUtils.chkSpecialCharacter(sku)) {
			
			message.setMessageTitle("Không thể "+phrase+" tài khoản, vui lòng thử lại sau!");
			return false;
		}
		
		Optional<Product> productOptional = this.productRepository.findById(sku);
		
		Product product = productOptional.isPresent() ? productOptional.get() : null;
		
		if (null == product) {
			
			message.setMessageTitle("Sản phẩm có SKU: " +sku + " không tồn tại hoặc đã bị xoá!");
			return false;
		}

		if (product.getIsAvailabled() == 0 && !isActive) {
			
			message.setMessageTitle("Sản phẩm đang ở trạng thái "+phrase+", yêu cầu " +phrase+" của bạn không được thực thi!");
			
		}
		
		if (product.getIsAvailabled() == 1 && isActive) {
			
			message.setMessageTitle("Sản phẩm đang ở trạng thái "+phrase+", yêu cầu " +phrase+" của bạn không được thực thi!");
			
		}
		
		product.setIsAvailabled(isActive ? 1: 0);;
		String updateDate = YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(YeeDateTimeUtils.getCurrentDateTime());

		product = this.productRepository.save(product);
		
		if (null == product) {
			
			message.setMessageTitle("Không thể "+phrase+" tài sản phẩm, vui lòng thử lại sau!");
			return false;
		}

		return true;
	}

	@Override
	public void initProductDto(EditProductDto productDto) {
		
		productDto.setMessage(YeeStringUtils.EMPTY);	
		List<Product> products = this.productRepository.findByProductNm(productDto.getProductNm());
		
		productDto.setProductNm(products.get(0).getProductNm());
		
		if (products.get(0).getBrand() != null) {
			
			productDto.setBrandCd(products.get(0).getBrand().getBrandCd());
		}
		productDto.setBrands(new ArrayList<>());
		CategoryDto noneBrand = new CategoryDto();
		
		noneBrand.setCateCd("0");
		noneBrand.setCateNm("No Brand");
		
		productDto.getBrands().add(noneBrand);
		if (YeeStringUtils.isEmpty(productDto.getBrandCd())) {
			
			productDto.setBrandCd("0");
		}
		// brand list
		List<Brand> brands = this.brandRepository.findAll();
		
		brands.stream().forEach(b -> {
			
			CategoryDto brand = new CategoryDto();
			
			brand.setCateCd(b.getBrandCd());
			brand.setCateNm(b.getBrandNm());
			
			productDto.getBrands().add(brand);
		});
		
		productDto.setMainCategories(new ArrayList<>());
		CategoryDto defaultCate = new CategoryDto();
		defaultCate.setCateCd("0");
		defaultCate.setCateNm("Đang cập nhật");
		productDto.getMainCategories().add(defaultCate);
		List<Category> cates = this.categoryRepository.findAll();

		cates.stream().forEach(c -> {
			
			CategoryDto cate = new CategoryDto();
			
			cate.setCateCd(c.getCateCd());
			cate.setCateNm(c.getCateNm());
			
			productDto.getMainCategories().add(cate);
		});

		if (products.get(0).getProductSeries() == null && YeeStringUtils.equals(productDto.getMainCateCd(), "0")) { 
			
			productDto.setMainCateCd("0");
		}
		if (YeeStringUtils.isEmpty(productDto.getMainCateCd())) {
			
			if (products.get(0).getProductSeries() != null) {
				
				productDto.setMainCateCd(products.get(0).getProductSeries().getSubCategory().getCategory().getCateCd());
			}
			
		}
		
		if (!YeeStringUtils.isEmpty(productDto.getMainCateCd())  && !YeeStringUtils.equals(productDto.getMainCateCd(), "0")) {
			
			List<SubCategory> subCates = this.subCategoryRepository.findByCategoryId(productDto.getMainCateCd());
			
			productDto.setSubCategories(new ArrayList<>());
			subCates.stream().forEach(sc -> {
				
				CategoryDto subCate = new CategoryDto();
				
				subCate.setCateCd(sc.getSubCateCd());
				subCate.setCateNm(sc.getSubCateNm());
				
				productDto.getSubCategories().add(subCate);
			});
			
			if (YeeStringUtils.isEmpty(productDto.getSubCateCd()) || YeeStringUtils.equals(productDto.getSubCateCd(), "0")) {
				
				if (products.get(0).getProductSeries() != null) {
					
					productDto.setSubCateCd(products.get(0).getProductSeries().getSubCategory().getSubCateCd());
					
				} else {

					productDto.setSubCateCd(subCates.get(0).getSubCateCd());
				}
				
			}
			
			
			
			
			// series
			productDto.setSeries(new ArrayList<>());
			List<ProductSeries> series = this.productSeriesRepository.findProductSeriesBySubCateCd(productDto.getSubCateCd());
			series.stream().forEach(s -> {
				
				CategoryDto serie = new CategoryDto();
				
				serie.setCateCd(s.getPSeriesCd());
				serie.setCateNm(s.getPSeriesNm());
				
				productDto.getSeries().add(serie);
			});
			
			if (YeeStringUtils.isEmpty(productDto.getSerieCd())) {
				
				if (products.get(0).getProductSeries() != null) {
					
					productDto.setSerieCd(products.get(0).getProductSeries().getPSeriesCd());
				} else {
					productDto.setSerieCd(series.get(0).getPSeriesCd());
				}
				
			}
		} else {
			
			productDto.setSeries(new ArrayList<>());
			productDto.setSubCategories(new ArrayList<>());
			productDto.setSubCateCd(YeeStringUtils.EMPTY);;
			productDto.setSerieCd(YeeStringUtils.EMPTY);
			
			CategoryDto subCate = new CategoryDto();
			CategoryDto serie = new CategoryDto();
			serie.setCateCd("0");
			serie.setCateNm("Đang cập nhật");
			subCate.setCateCd("0");
			subCate.setCateNm("Đang cập nhật");
			productDto.setMainCateCd("0");			
			productDto.getSeries().add(serie);
			productDto.getSubCategories().add(subCate);
		}

		// storage
		if (YeeStringUtils.isEmpty(productDto.getStorage())) {
			
			productDto.setStorage(products.get(0).getProductStorage());
		}
		
		List<String> storages = initStorage();
		
		productDto.setStorages(storages);
		
		productDto.setProductDspt(products.get(0).getProductDescription());
		
		productDto.setSubEditProducts(new ArrayList<>());
		List<String> colors =  initColors();
		products.stream().forEach(p -> {
			
			SubEditProductDto subDto = new SubEditProductDto();
			
			subDto.setProductId(p.getSKU());
			subDto.setColor(p.getProductColor());
			subDto.setProductImage(p.getProductImageColor());
			subDto.setUnitPrice(p.getUnitPrice());
			subDto.setQty(p.getProductQuantity());
			subDto.setIsAvailable(p.getIsAvailabled());
			
			productDto.getSubEditProducts().add(subDto);
			
			Boolean isColorPresent = colors.stream()
	                .anyMatch(element -> element.equals(subDto.getColor()));
			
			if (!isColorPresent) {
				
				colors.add(subDto.getColor());
			}
		});
		
		productDto.setColors(colors);

		if (YeeStringUtils.isEmpty(productDto.getProductSubImageDto().getPImageId())) {
			
			productDto.getProductSubImageDto().setPImageId(products.get(0).getProductImage().getImageId());
		}	
		
		if (YeeStringUtils.isEmpty(productDto.getProductSubImageDto().getMainImage())) {
			
			productDto.getProductSubImageDto().setMainImage(products.get(0).getProductImage().getMainImage());
			
		}
		
		if (YeeStringUtils.isEmpty(productDto.getProductSubImageDto().getImage1())) {
			
			productDto.getProductSubImageDto().setImage1(products.get(0).getProductImage().getImage1());
			
		}
		
		if (YeeStringUtils.isEmpty(productDto.getProductSubImageDto().getImage2())) {
			
			productDto.getProductSubImageDto().setImage2(products.get(0).getProductImage().getImage2());
			
		}
		
		if (YeeStringUtils.isEmpty(productDto.getProductSubImageDto().getImage3())) {
			
			productDto.getProductSubImageDto().setImage3(products.get(0).getProductImage().getImage3());
			
		}
		
		if (YeeStringUtils.isEmpty(productDto.getDateStart())) {
			
			productDto.setDateStart(products.get(0).getDateStart());
			
		}
		
		productDto.setTempDate(products.get(0).getDateStart());		

		if(null != products.get(0).getSale()) {
			
			long a = YeeDateTimeUtils.calcDaysBetween2Dates(products.get(0).getSale().getDayEnd(),productDto.getDateStart());
			if (YeeDateTimeUtils.calcDaysBetween2Dates(products.get(0).getSale().getDayEnd(),productDto.getDateStart()) <= 0 ) {
				
				productDto.setIsSale(true);
				productDto.setSales(products.get(0).getSale());
			} else {
				
				productDto.setIsSale(false);
				productDto.setSales(new Sales());
			}
		}
		
		ProductAttribute productAttribute = products.get(0).getProductAttribute();

		this.modelMapper.map(productAttribute, productDto);
		
	}
	
	private List<String> initStorage(){
		
		List<String> storages = new ArrayList<>();
		storages.add("16GB");
		storages.add("32GB");
		storages.add("64GB");
		storages.add("128GB");
		storages.add("256GB");
		storages.add("512GB");
		storages.add("1TB");
		
		return storages;
	}
	
	private List<String> initColors(){
		
		List<String> colors = new ArrayList<>();
		colors.add("Đỏ");
		colors.add("Xanh Đen");
		colors.add("Xám Đen");
		colors.add("Xanh Ngọc");
		colors.add("Tím");
		
		return colors;
	}

	@Override
	public Boolean updateProduct(EditProductDto productDto, YeeMessage yeeMessage) {
		
		Boolean chkExistAnother = false;
		yeeMessage.setMessageTitle(YeeStringUtils.EMPTY);	
		List<Product> products = this.productRepository.findProductsBySeries(productDto.getSerieCd());
		List<Product> products2 = this.productRepository.findProductsExceptSeries(productDto.getSerieCd());
		
//		for (Product product : products2) {
//			
//			for (SubEditProductDto sp: productDto.getSubEditProducts()) {
//				
//				if (YeeStringUtils.equals(sp.getColor(), product.getProductColor()) 
//						&& YeeStringUtils.equals(productDto.getStorage(),product.getProductStorage())) {
//					
//					yeeMessage.setMessageTitle("Đã tồn tại sản phẩm có dung lượng "+productDto.getStorage()+ " và màu " + sp.getColor());
//					chkExistAnother = true;
//				}
//				
//			}
//		}
		for (Product product : products) {
			
			for (SubEditProductDto sp: productDto.getSubEditProducts()) {
				
				if (!YeeStringUtils.equals(sp.getProductId(), product.getSKU())) {
					if (YeeStringUtils.equals(sp.getColor(), product.getProductColor()) && YeeStringUtils.equals(productDto.getStorage(),product.getProductStorage())) {
						
						yeeMessage.setMessageTitle("Cùng một dòng sản phẩm, đã tồn tại sản phẩm có dung lượng "+productDto.getStorage()+ " và màu " + sp.getColor());
						chkExistAnother = true;
						
						
					}
				}
				
				if (!this.productSeriesRepository.existsById(productDto.getSerieCd())) {
					
					yeeMessage.setMessageTitle("Series sản phẩm không tồn tại");
					chkExistAnother = true;
					
				}
				
				if (!this.subCategoryRepository.existsById(productDto.getSubCateCd())) {
					
					yeeMessage.setMessageTitle("Danh mục phụ không tồn tại");
					chkExistAnother = true;
					
				}
				
				if (!this.categoryRepository.existsById(productDto.getMainCateCd())) {
					
					yeeMessage.setMessageTitle("Danh mục chính không tồn tại");
					chkExistAnother = true;
					
				}
				
				if (!this.brandRepository.existsById(productDto.getBrandCd())) {
					
					yeeMessage.setMessageTitle("Nhà cung cấp không tồn tại");
					chkExistAnother = true;
					
				}

			}
		}
		
		if (chkExistAnother) {
			
			return false;
		}
		
		products = new ArrayList<>();
		String crrDate = YeeDateTimeUtils.getCurrentDateTime();
		Integer baseLoop = 0;
		for (SubEditProductDto sp: productDto.getSubEditProducts()) {
			Product p = new Product();
			if (YeeStringUtils.isEmpty(sp.getProductId())) {
				sp.setProductId("SKU"+YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(crrDate)+ YeeStringUtils.format3DigistNumber(baseLoop));
				p.setSKU("SKU"+YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(crrDate)+ YeeStringUtils.format3DigistNumber(baseLoop));
			} else {
				
				Optional<Product> pOptional = this.productRepository.findById(sp.getProductId());
				p = pOptional.isPresent() ? pOptional.get() : new Product()	;
				
				if (YeeStringUtils.isEmpty(p.getSKU())) {
					
					sp.setProductId("SKU"+YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(crrDate)+ YeeStringUtils.format3DigistNumber(baseLoop));
					p.setSKU("SKU"+YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(crrDate)+ YeeStringUtils.format3DigistNumber(baseLoop));
				}
			}

			// product name
			p.setProductNm(productDto.getProductNm());
			
			// product storage
			p.setProductStorage(productDto.getStorage());
			
			// product series
			Optional<ProductSeries> psOtiOptional = this.productSeriesRepository.findById(productDto.getSerieCd());
			ProductSeries pSeries = psOtiOptional.isEmpty() ? null : psOtiOptional.get();
			p.setProductSeries(pSeries);
			
			// brand
			Optional<Brand> brandOptional = this.brandRepository.findById(productDto.getBrandCd());
			Brand brand = brandOptional.isEmpty() ? null : brandOptional.get();		
			p.setBrand(brand);
			
			// product Description
			p.setProductDescription(productDto.getProductDspt());	
			
			// product color
			p.setProductColor(sp.getColor());
			
			// product Image
			p.setProductImageColor(sp.getProductImage());
			
			// product unitPrice 
			p.setUnitPrice(sp.getUnitPrice());
			
			// product quantity
			p.setProductQuantity(sp.getQty());
			
			// product available
			p.setIsAvailabled(sp.getIsAvailable());
			
			// sub image
			ProductImage productImage = new ProductImage();
			
			if (YeeStringUtils.isEmpty(productDto.getProductSubImageDto().getPImageId())) {
				
				productDto.getProductSubImageDto().setPImageId("IMG"+crrDate);
			}
			productImage.setImageId(productDto.getProductSubImageDto().getPImageId());
			
			productImage.setMainImage(productDto.getProductSubImageDto().getMainImage());
			
			productImage.setImage1(productDto.getProductSubImageDto().getImage1());
			productImage.setImage2(productDto.getProductSubImageDto().getImage2());			
			productImage.setImage3(productDto.getProductSubImageDto().getImage3());			
			p.setProductImage(productImage);
			productImage = this.productImageRepository.save(productImage);
			// start sold date 
			p.setDateStart(productDto.getDateStart());
			
			if (productDto.getIsSale() && productDto.getSales() != null ) { 
				
				p.setSale(productDto.getSales());
			} else {
				
				p.setSale(null);			}
			
			// product properties
			if (YeeStringUtils.isEmpty(productDto.getPAttrId())) {
				
				productDto.setPAttrId("ATTR"+ crrDate);
			}
			ProductAttribute pAttribute = this.modelMapper.map(productDto, ProductAttribute.class);
			pAttribute = this.productAttributeRepository.save(pAttribute);
			
			if (null == pAttribute) {
				
				return false;
			}
			p.setProductAttribute(pAttribute);
			
			if (p.getSoldQty() == null) {

				p.setSoldQty(INT_0);
			}
			
			if (p.getProductViewCnt() == null) {
				
				p.setProductViewCnt(INT_0);
			}
			p.setPState(INT_1);
			products.add(p);
			baseLoop++;
		}
		
		try {
			
			this.productRepository.saveAll(products);
		} catch (Exception e) {

			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình cập nhật thông tin sản phẩm. Vui lòng thử lại!");
			return false;
		}
		
		
		return true;
	}

	@Override
	public void initProductDtoNew(EditProductDto productDto) {
		
		
		// brand list
		List<Brand> brands = this.brandRepository.findAll();
		
		productDto.setBrands(new ArrayList<>());
		CategoryDto noneBrand = new CategoryDto();
		
		noneBrand.setCateCd("0");
		noneBrand.setCateNm("No Brand");
		
		productDto.getBrands().add(noneBrand);
		
		brands.stream().forEach(b -> {
			
			CategoryDto brand = new CategoryDto();
			
			brand.setCateCd(b.getBrandCd());
			brand.setCateNm(b.getBrandNm());
			
			productDto.getBrands().add(brand);
		});

		if (!this.brandRepository.existsById(productDto.getBrandCd())) {
			
			productDto.setBrandCd(brands.get(0).getBrandCd());
		}
		
		
		List<Category> cates = this.categoryRepository.findAll();
		productDto.setMainCategories(new ArrayList<>());
		CategoryDto noneCate = new CategoryDto();
		noneCate.setCateCd("0");
		noneCate.setCateNm("Đang cập nhật");
		productDto.getMainCategories().add(noneCate);
	
		cates.stream().forEach(c -> {
				
			CategoryDto cate = new CategoryDto();
			
			cate.setCateCd(c.getCateCd());
			cate.setCateNm(c.getCateNm());
			
			productDto.getMainCategories().add(cate);
		});

		if (!this.categoryRepository.existsById(productDto.getMainCateCd())) {
			
			productDto.setMainCateCd(productDto.getMainCategories().get(0).getCateCd());
		}
		
		if (!YeeStringUtils.isEmpty(productDto.getMainCateCd())  && !YeeStringUtils.equals(productDto.getMainCateCd(), "0")) {
			
			List<SubCategory> subCates = this.subCategoryRepository.findByCategoryId(productDto.getMainCateCd());
			
			productDto.setSubCategories(new ArrayList<>());
			subCates.stream().forEach(sc -> {
				
				CategoryDto subCate = new CategoryDto();
				
				subCate.setCateCd(sc.getSubCateCd());
				subCate.setCateNm(sc.getSubCateNm());
				
				productDto.getSubCategories().add(subCate);
			});
			
			if (!this.subCategoryRepository.existsById(productDto.getSubCateCd())) {
				
				productDto.setSubCateCd(productDto.getSubCategories().get(0).getCateCd());
			}
			
			// series
			productDto.setSeries(new ArrayList<>());
			List<ProductSeries> series = this.productSeriesRepository.findProductSeriesBySubCateCd(productDto.getSubCateCd());
			series.stream().forEach(s -> {
				
				CategoryDto serie = new CategoryDto();
				
				serie.setCateCd(s.getPSeriesCd());
				serie.setCateNm(s.getPSeriesNm());
				
				productDto.getSeries().add(serie);
			});
			
			if (!this.productSeriesRepository.existsById(productDto.getSerieCd())) {
				
				productDto.setSerieCd(productDto.getSeries().get(0).getCateCd());
			}
		} else {
			
			productDto.setSeries(new ArrayList<>());
			productDto.setSubCategories(new ArrayList<>());
			productDto.setSubCateCd(YeeStringUtils.EMPTY);;
			productDto.setSerieCd(YeeStringUtils.EMPTY);
			
			CategoryDto subCate = new CategoryDto();
			CategoryDto serie = new CategoryDto();
			serie.setCateCd("0");
			serie.setCateNm("Đang cập nhật");
			subCate.setCateCd("0");
			subCate.setCateNm("Đang cập nhật");
			productDto.setMainCateCd("0");			
			productDto.getSeries().add(serie);
			productDto.getSubCategories().add(subCate);
		}

		// storage
		List<String> storages = this.initStorage();
;
		productDto.setStorages(storages);
		
		if (productDto.getStorage() != null) {
			
			productDto.setStorage(productDto.getStorages().get(0));
		}		
		List<String> colors =  initColors();

		productDto.setColors(colors);
		
		if (YeeStringUtils.isEmpty(productDto.getDateStart())) {
			
			productDto.setDateStart(YeeDateTimeUtils.getCurrentDate());
			
		}
		
		productDto.setTempDate(YeeDateTimeUtils.getCurrentDate());
		
		if (productDto.getSubEditProducts() == null) {
			
			productDto.setSubEditProducts(new ArrayList<>());	
		}
		
		if (productDto.getProductSubImageDto() != null) {
			
			productDto.setProductSubImageDto(new ProductImageDto());
		}
	}

	@Override
	public List<SubEditProductDto> getProductsByNm(HttpServletRequest request) {

		List<SubEditProductDto> subEditProductDtos = new ArrayList<>();
		String productNm = request.getParameter("productNm");
		
		List<Product> products = this.productRepository.findByProductNm(productNm);
		
		products.stream().forEach(e -> {
			
			SubEditProductDto subEditProductDto = new SubEditProductDto();
			
			subEditProductDto.setProductId(e.getSKU());
			subEditProductDto.setColor(e.getProductColor());
			subEditProductDto.setProductImage(e.getProductImageColor());					
			subEditProductDtos.add(subEditProductDto);
			
		});
		
		return subEditProductDtos;
		
	}

	@Override
	public Boolean deleteProducts(List<String> idItems, YeeMessage yeeMessage) {
		
		
		idItems.stream().forEach(e -> {
			
			List<OrderDetails> orderDetails = this.orderDetailsRepository.findByProductId(e);
			
			orderDetails.stream().forEach(o -> {
				
				o.setProduct(null);
			});
			
			orderDetails = this.orderDetailsRepository.saveAll(orderDetails);
		});
		this.productRepository.deleteAllById(idItems);
		
		yeeMessage.setMessageTitle("Xoá sản phẩm thành công!");
		return true;
	}
}
