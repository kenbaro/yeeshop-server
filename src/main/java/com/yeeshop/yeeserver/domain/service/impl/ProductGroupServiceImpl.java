package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeeshop.yeeserver.domain.admin.dto.productmanage.CategoryDto;
import com.yeeshop.yeeserver.domain.dto.productgroup.ProductGroupDto;
import com.yeeshop.yeeserver.domain.entity.Category;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;
import com.yeeshop.yeeserver.domain.entity.SubCategory;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.ICategoryRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.repository.IProductSeriesRepository;
import com.yeeshop.yeeserver.domain.repository.ISubCategoryRepository;
import com.yeeshop.yeeserver.domain.service.IProductGroupService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class ProductGroupServiceImpl implements IProductGroupService{
	
	@Autowired private IProductSeriesRepository productSeriesRepository;

	@Autowired private ICategoryRepository categoryRepository;
	
	@Autowired private ISubCategoryRepository subCategoryRepository;
	
	@Autowired private IProductRepository productRepository;
	@Override
	public List<ProductGroupDto> getAllProductGroup() {
		
		List<ProductSeries> productSeries = this.productSeriesRepository.findAll();
		List<ProductGroupDto> productGroupDtos = new ArrayList<>();

		productSeries.stream().forEach(e -> {
			
			ProductGroupDto productGroupDto = new ProductGroupDto();
			
			productGroupDto.setPSeriesId(e.getPSeriesCd());
			
			productGroupDto.setPSeriesNm(e.getPSeriesNm());
			
			productGroupDto.setPSubCateId(e.getSubCategory().getSubCateCd());
			
			productGroupDto.setPSubCateNm(e.getSubCategory().getSubCateNm());
			
			productGroupDto.setPCateId(e.getSubCategory().getCategory().getCateCd());
			
			productGroupDto.setPCateNm(e.getSubCategory().getCategory().getCateNm());
			
			productGroupDtos.add(productGroupDto);
			
		});
		
		Map<String, Long> subCateCountMap =  productGroupDtos.stream().collect(Collectors.groupingBy(ProductGroupDto::getPSubCateId,Collectors.counting()));
		Map<String, Long> cateCountMap =  productGroupDtos.stream().collect(Collectors.groupingBy(ProductGroupDto::getPCateId,Collectors.counting()));
		
		productGroupDtos.stream().forEach(e-> {
			
			e.setSubCateRowSpan(subCateCountMap.get(e.getPSubCateId()));
			e.setCateRowSpan(cateCountMap.get(e.getPCateId()));
		});
		return productGroupDtos;
	}

	@Override
	public void getAllMainCategory(HttpServletRequest request, ProductGroupDto productGroupDto) {
		
		String param = request.getParameter("param");
		
		if (YeeStringUtils.equals(param, "1")) {
			
			productGroupDto.setMainCates(new ArrayList<>());
			productGroupDto.setSeries(new ArrayList<>());
			productGroupDto.setSubCates(new ArrayList<>());
			productGroupDto.setPSeriesId(YeeStringUtils.EMPTY);
			productGroupDto.setPSeriesNm(YeeStringUtils.EMPTY);
			productGroupDto.setPCateId(YeeStringUtils.EMPTY);
			productGroupDto.setPCateNm(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateId(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateNm(YeeStringUtils.EMPTY);
		}
		
		if (YeeStringUtils.equals(param, "2")) {
			
			List<CategoryDto> mainCate = new ArrayList<>();

			List<Category> cates = this.categoryRepository.findAll();
			
				cates.stream().forEach(c -> {
				
				CategoryDto cate = new CategoryDto();
				
				cate.setCateCd(c.getCateCd());
				cate.setCateNm(c.getCateNm());
				
				mainCate.add(cate);
			});
			
			if(YeeStringUtils.isEmpty(productGroupDto.getPCateId())) {
		    	
		    	productGroupDto.setPCateId(mainCate.get(0).getCateCd());
		    	productGroupDto.setPCateNm(mainCate.get(0).getCateNm());
		    }
			productGroupDto.setPSeriesId(YeeStringUtils.EMPTY);
			productGroupDto.setPSeriesNm(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateId(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateNm(YeeStringUtils.EMPTY);

			productGroupDto.setMainCates(mainCate);
		}

		if (YeeStringUtils.equals(param, "3")) {
			
			List<CategoryDto> mainCate = new ArrayList<>();
			List<Category> cates = this.categoryRepository.findAll();
			
			cates.stream().forEach(c -> {
			
				CategoryDto cate = new CategoryDto();
				
				cate.setCateCd(c.getCateCd());
				cate.setCateNm(c.getCateNm());
				
				mainCate.add(cate);
			});
			
		    if(YeeStringUtils.isEmpty(productGroupDto.getPCateId())) {
		    	
		    	productGroupDto.setPCateId(mainCate.get(0).getCateCd());
		    	productGroupDto.setPCateNm(mainCate.get(0).getCateNm());
		    }
		    
		    productGroupDto.setPSeriesId(YeeStringUtils.EMPTY);
			productGroupDto.setPSeriesNm(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateId(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateNm(YeeStringUtils.EMPTY);

			productGroupDto.setMainCates(mainCate);
			
			List<CategoryDto> subCate = new ArrayList<>();
			List<SubCategory> subCates = this.subCategoryRepository.findByCategoryId(productGroupDto.getPCateId());
			
			subCates.stream().forEach(c -> {
				
				CategoryDto cate = new CategoryDto();
				
				cate.setCateCd(c.getSubCateCd());
				cate.setCateNm(c.getSubCateNm());
				
				subCate.add(cate);
			});

			if(YeeStringUtils.isEmpty(productGroupDto.getPSubCateId())) {
		    	
		    	productGroupDto.setPSubCateId(subCate.get(0).getCateCd());
		    	productGroupDto.setPSubCateNm(subCate.get(0).getCateNm());
		    }

			productGroupDto.setSubCates(subCate);
		}
		
	}

	@Override
	public Boolean addCate(HttpServletRequest request, ProductGroupDto productGroupDto, YeeMessage msg) {
		
		msg.setMessageTitle(YeeStringUtils.EMPTY);
		String param = request.getParameter("param");
		
		if (YeeStringUtils.equals(param, "1")) {
			
			if (YeeStringUtils.isEmpty(productGroupDto.getPCateNm())) {
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}

			if (YeeStringUtils.isEmpty(productGroupDto.getPSubCateNm())) {
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			if (YeeStringUtils.isEmpty(productGroupDto.getPSeriesNm())) {
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			List<Category> chkListCate = this.categoryRepository.findByCateNm(productGroupDto.getPCateNm());
			
			if (chkListCate.size() > 0) {
				
				msg.setMessageTitle("Danh mục chính đã tồn tại!");
				return false;
			}

			String crrDateTime = YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(YeeDateTimeUtils.getCurrentDateTime());

			Category category = new Category();
			category.setShowFlg(1);
			category.setCateCd("CATE"+crrDateTime);
			category.setCateNm(productGroupDto.getPCateNm());
			
			SubCategory subCategory = new SubCategory();
			subCategory.setSubCateCd("SCAT"+crrDateTime);
			subCategory.setSubCateNm(productGroupDto.getPSubCateNm());
			subCategory.setCategory(category);
			
			ProductSeries productSeries = new ProductSeries();
			productSeries.setPSeriesCd("SR"+crrDateTime);
			productSeries.setPSeriesNm(productGroupDto.getPSeriesNm());
			productSeries.setSubCategory(subCategory);
			
			try {
				
				category = this.categoryRepository.save(category);
				subCategory= this.subCategoryRepository.save(subCategory);
				productSeries = this.productSeriesRepository.save(productSeries);
			}
			catch (Exception e) {
				
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}

			msg.setMessageTitle("Thêm nhóm sản phẩm thành công");
			return true;
		}
		
		if (YeeStringUtils.equals(param, "2")) {
			
			if (!this.categoryRepository.existsById(productGroupDto.getPCateId())) {
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}

			if (YeeStringUtils.isEmpty(productGroupDto.getPSubCateNm())) {
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			if (YeeStringUtils.isEmpty(productGroupDto.getPSeriesNm())) {
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			List<ProductSeries> chkList = this.productSeriesRepository.findByPSeriesNm(productGroupDto.getPSeriesNm());
			
			if (chkList.size() > 0
					&& YeeStringUtils.equals(chkList.get(0).getSubCategory().getSubCateNm(), productGroupDto.getPSubCateNm())
					&& YeeStringUtils.equals(chkList.get(0).getSubCategory().getCategory().getCateCd(), productGroupDto.getPCateId())) {
				
				msg.setMessageTitle("Nhóm sản phẩm này đã tồn tại!!!");
				return false;
			}
			
			String crrDateTime = YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(YeeDateTimeUtils.getCurrentDateTime());

			Category category = new Category();
			category.setShowFlg(1);
			category.setCateCd(productGroupDto.getPCateId());
			category.setCateNm(productGroupDto.getPCateNm());
			
			SubCategory subCategory = new SubCategory();
			subCategory.setSubCateCd("SCAT"+crrDateTime);
			subCategory.setSubCateNm(productGroupDto.getPSubCateNm());
			subCategory.setCategory(category);
			
			ProductSeries productSeries = new ProductSeries();
			productSeries.setPSeriesCd("SR"+crrDateTime);
			productSeries.setPSeriesNm(productGroupDto.getPSeriesNm());
			productSeries.setSubCategory(subCategory);
			
			try {

				subCategory= this.subCategoryRepository.save(subCategory);
				productSeries = this.productSeriesRepository.save(productSeries);
			}
			catch (Exception e) {
				
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}

			msg.setMessageTitle("Thêm nhóm sản phẩm thành công");
			return true;
		}

		if (YeeStringUtils.equals(param, "3")) {
			
			if (!this.categoryRepository.existsById(productGroupDto.getPCateId())) {
				
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}

			if (!this.subCategoryRepository.existsById(productGroupDto.getPSubCateId())) {
				
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			if (YeeStringUtils.isEmpty(productGroupDto.getPSeriesNm())) {
				
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			List<ProductSeries> chkList = this.productSeriesRepository.findByPSeriesNm(productGroupDto.getPSeriesNm());
			
			if (chkList.size() > 0
					&& YeeStringUtils.equals(chkList.get(0).getSubCategory().getSubCateCd(), productGroupDto.getPSubCateId())
					&& YeeStringUtils.equals(chkList.get(0).getSubCategory().getCategory().getCateCd(), productGroupDto.getPCateId())) {
				
				msg.setMessageTitle("Nhóm sản phẩm này đã tồn tại!!!");
				return false;
			}
			
			String crrDateTime = YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(YeeDateTimeUtils.getCurrentDateTime());

			Category category = new Category();
			category.setShowFlg(1);
			category.setCateCd(productGroupDto.getPCateId());
			category.setCateNm(productGroupDto.getPCateNm());
			
			SubCategory subCategory = new SubCategory();
			subCategory.setSubCateCd(productGroupDto.getPSubCateId());
			subCategory.setSubCateNm(productGroupDto.getPSubCateNm());
			subCategory.setCategory(category);
			
			ProductSeries productSeries = new ProductSeries();
			productSeries.setPSeriesCd("SR"+crrDateTime);
			productSeries.setPSeriesNm(productGroupDto.getPSeriesNm());
			productSeries.setSubCategory(subCategory);
			
			try {

				productSeries = this.productSeriesRepository.save(productSeries);
			}
			catch (Exception e) {
				
				msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			msg.setMessageTitle("Thêm nhóm sản phẩm thành công");
			return true;
		}
		
		msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
		return false;
	}

	@Override
	public Boolean updateCate(ProductGroupDto productGroupDto, YeeMessage msg) {

		if(!this.categoryRepository.existsById(productGroupDto.getPCateId())) {
			
			msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		if (!this.subCategoryRepository.existsById(productGroupDto.getPSubCateId())) {
			
			msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		if (!this.productSeriesRepository.existsById(productGroupDto.getPSeriesId())) {
			
			msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		List<Category> chkListCate = this.categoryRepository.findByCateNm(productGroupDto.getPCateNm());
		
		if (chkListCate.size() > 0 && !chkListCate.get(0).getCateCd().equals(productGroupDto.getPCateId())) {
			
			msg.setMessageTitle("Danh mục chính đã tồn tại!");
			return false;
		}
		
		Category category = new Category();
		category.setShowFlg(1);
		category.setCateCd(productGroupDto.getPCateId());
		category.setCateNm(productGroupDto.getPCateNm());
		
		SubCategory subCategory = new SubCategory();
		subCategory.setSubCateCd(productGroupDto.getPSubCateId());
		subCategory.setSubCateNm(productGroupDto.getPSubCateNm());
		subCategory.setCategory(category);
		
		ProductSeries productSeries = new ProductSeries();
		productSeries.setPSeriesCd(productGroupDto.getPSeriesId());
		productSeries.setPSeriesNm(productGroupDto.getPSeriesNm());
		productSeries.setSubCategory(subCategory);
		try {
			
			category = this.categoryRepository.save(category);
			subCategory= this.subCategoryRepository.save(subCategory);
			productSeries = this.productSeriesRepository.save(productSeries);
		}
		catch (Exception e) {
			
			msg.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}

		msg.setMessageTitle("Cập nhật nhóm sản phẩm thành công");
		return true;
	}

	@Override
	public void getProductGroupById(HttpServletRequest request, ProductGroupDto productGroupDto) {
		
		String param = request.getParameter("param");
		String cateId = request.getParameter("cId");
		String subCateId = request.getParameter("scId");
		
		if (YeeStringUtils.equals(param, "1")) {
			
			productGroupDto.setMainCates(new ArrayList<>());
			productGroupDto.setSeries(new ArrayList<>());
			productGroupDto.setSubCates(new ArrayList<>());
			productGroupDto.setPSeriesId(YeeStringUtils.EMPTY);
			productGroupDto.setPSeriesNm(YeeStringUtils.EMPTY);
			productGroupDto.setPCateId(YeeStringUtils.EMPTY);
			productGroupDto.setPCateNm(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateId(YeeStringUtils.EMPTY);
			productGroupDto.setPSubCateNm(YeeStringUtils.EMPTY);
		}
		
		if (YeeStringUtils.equals(param, "2")) {
			
			if (!YeeStringUtils.isEmpty(cateId)) {
				
				List<CategoryDto> mainCate = new ArrayList<>();
				Map<String, String> existMap = new HashMap<>();
				existMap.put(cateId, YeeStringUtils.EMPTY);
				List<Category> cates = this.categoryRepository.findAll();
				
					cates.stream().forEach(c -> {
					if (YeeStringUtils.equals(c.getCateCd(), cateId)) {
						
						String nm = c.getCateNm();
						existMap.put(cateId, nm);
					}
					CategoryDto cate = new CategoryDto();
					
					cate.setCateCd(c.getCateCd());
					cate.setCateNm(c.getCateNm());
					
					mainCate.add(cate);
				});
				
				if(YeeStringUtils.isEmpty(cateId)) {
			    	
			    	productGroupDto.setPCateId(mainCate.get(0).getCateCd());
			    	productGroupDto.setPCateNm(mainCate.get(0).getCateNm());
			    } else {
			    	
			    	productGroupDto.setPCateId(cateId);
			    	productGroupDto.setPCateNm(existMap.get(cateId));
			    }
//				productGroupDto.setPSeriesId(YeeStringUtils.EMPTY);
//				productGroupDto.setPSeriesNm(YeeStringUtils.EMPTY);
				productGroupDto.setPSubCateId(YeeStringUtils.EMPTY);
				productGroupDto.setPSubCateNm(YeeStringUtils.EMPTY);

				productGroupDto.setMainCates(mainCate);
			}
		}

		if (YeeStringUtils.equals(param, "3")) {
			
			if (YeeStringUtils.isNotEmpty(cateId)) {
				
				List<CategoryDto> mainCate = new ArrayList<>();
				List<Category> cates = this.categoryRepository.findAll();
				Map<String, String> existMap = new HashMap<>();
				existMap.put(cateId, YeeStringUtils.EMPTY);
				cates.stream().forEach(c -> {
				
					CategoryDto cate = new CategoryDto();
					if (YeeStringUtils.equals(c.getCateCd(), cateId)) {
						
						String nm = c.getCateNm();
						existMap.put(cateId, nm);
					}
					cate.setCateCd(c.getCateCd());
					cate.setCateNm(c.getCateNm());
					
					mainCate.add(cate);
				});
				
			    if(YeeStringUtils.isEmpty(cateId)) {
			    	
			    	productGroupDto.setPCateId(mainCate.get(0).getCateCd());
			    	productGroupDto.setPCateNm(mainCate.get(0).getCateNm());
			    } else {
			    	
			    	productGroupDto.setPCateId(cateId);
			    	productGroupDto.setPCateNm(existMap.get(cateId));
			    }
			    
//			    productGroupDto.setPSeriesId(YeeStringUtils.EMPTY);
//				productGroupDto.setPSeriesNm(YeeStringUtils.EMPTY);
//				productGroupDto.setPSubCateId(YeeStringUtils.EMPTY);
//				productGroupDto.setPSubCateNm(YeeStringUtils.EMPTY);

				productGroupDto.setMainCates(mainCate);
				
				List<CategoryDto> subCate = new ArrayList<>();
				List<SubCategory> subCates = this.subCategoryRepository.findByCategoryId(productGroupDto.getPCateId());
				
				
				existMap.put(subCateId, YeeStringUtils.EMPTY);
				subCates.stream().forEach(c -> {
					
					if (YeeStringUtils.equals(c.getSubCateCd(), subCateId)) {
						
						String nm = c.getSubCateNm();
						existMap.put(subCateId, nm);
					}
					CategoryDto cate = new CategoryDto();
					
					cate.setCateCd(c.getSubCateCd());
					cate.setCateNm(c.getSubCateNm());
					
					subCate.add(cate);
				});
				
				if(YeeStringUtils.isEmpty(existMap.get(subCateId))) {
			    	
			    	productGroupDto.setPSubCateId(subCate.get(0).getCateCd());
			    	productGroupDto.setPSubCateNm(subCate.get(0).getCateNm());
			    } else {
			    	
			    	productGroupDto.setPSubCateId(subCateId);
			    	productGroupDto.setPSubCateNm(existMap.get(subCateId));
			    }

				productGroupDto.setSubCates(subCate);
			}
		}
		
	}

	@Override
	public void getAllSubCategory(HttpServletRequest request, ProductGroupDto productGroupDto) {

		List<SubCategory> subCates = this.subCategoryRepository.findAll();
		List<CategoryDto> subCate = new ArrayList<>();
		subCates.stream().forEach(c -> {

			CategoryDto cate = new CategoryDto();
			
			cate.setCateCd(c.getSubCateCd());
			cate.setCateNm(c.getSubCateNm());
			
			subCate.add(cate);
		});
		productGroupDto.setSubCates(subCate);
	}

	@Override
	public Boolean deleteCate(HttpServletRequest request, YeeMessage msg) {

		msg.setMessageTitle(YeeStringUtils.EMPTY);

		String cId = request.getParameter("cId");
		String scId = request.getParameter("scId");
		String srId = request.getParameter("srId");
		
		if (YeeStringUtils.isAllEmpty(cId,scId,srId)) {
			
			msg.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
			return false;
		}
		
		if (YeeStringUtils.isNotEmpty(cId) && YeeStringUtils.isEmpty(srId) && YeeStringUtils.isEmpty(scId)) {
			
			Optional<Category> cateOptional = this.categoryRepository.findById(cId);
			
			Category category =  cateOptional.isPresent() ? cateOptional.get() : null;
			
			if (null == category) {
				
				msg.setMessageTitle("Danh mục chính không tồn tại hoặc đã bị xoá trước đó, vui lòng thử lại!");
				return false;
			}
			
			List<SubCategory> subCategories = this.subCategoryRepository.findByCategoryId(cId);
			
			subCategories.stream().forEach(e -> {
				
				List<ProductSeries> productSeries = this.productSeriesRepository.findProductSeriesBySubCateCd(e.getSubCateCd());
				
				productSeries.stream().forEach(sr -> {
					
					List<Product> products = this.productRepository.findProductsBySeries(sr.getPSeriesCd());
					products.stream().forEach(p -> {
						
						p.setProductSeries(null);
					});
					products = this.productRepository.saveAll(products);
				});
				
				this.productSeriesRepository.deleteAll(productSeries);
			});
			
			this.subCategoryRepository.deleteAll(subCategories);
			this.categoryRepository.delete(category);
			
			msg.setMessageTitle("Xoá danh mục chính thành công!");
			return true;
		}
		
		if (YeeStringUtils.isEmpty(cId) && YeeStringUtils.isEmpty(srId) && YeeStringUtils.isNotEmpty(scId)) {
			
			Optional<SubCategory> subcateOptional = this.subCategoryRepository.findById(scId);
			
			SubCategory subCategory = subcateOptional.isPresent() ? subcateOptional.get() : null;
			
			if (null == subCategory) {
				
				msg.setMessageTitle("Danh mục phụ không tồn tại hoặc đã bị xoá trước đó, vui lòng thử lại!");
				return false;
			}
			
			Category category = subCategory.getCategory();
			
			List<ProductSeries> productSeries = this.productSeriesRepository.findProductSeriesBySubCateCd(subCategory.getSubCateCd());
			
			productSeries.stream().forEach(sr -> {
				
				List<Product> products = this.productRepository.findProductsBySeries(sr.getPSeriesCd());
				products.stream().forEach(p -> {
					
					p.setProductSeries(null);
				});
				products = this.productRepository.saveAll(products);
			});
			
			this.productSeriesRepository.deleteAll(productSeries);
			this.subCategoryRepository.delete(subCategory);
			
			List<SubCategory> subCategories = this.subCategoryRepository.findByCategoryId(category.getCateCd());
			if (subCategories.size() == 0) {
				
				this.categoryRepository.delete(category);
			}
			
			msg.setMessageTitle("Xoá danh mục phụ thành công!");
			return true;
		}
		
		if (YeeStringUtils.isEmpty(cId) && YeeStringUtils.isNotEmpty(srId) && YeeStringUtils.isEmpty(scId)) {
			
			Optional<ProductSeries> productseriesOptional = this.productSeriesRepository.findById(srId);
			
			ProductSeries pSeries = productseriesOptional.isPresent() ? productseriesOptional.get() : null;
			
			if (null == pSeries) {
				
				msg.setMessageTitle("Serie sản phẩm không tồn tại hoặc đã bị xoá trước đó, vui lòng thử lại!");
				return false;
			}
			
			SubCategory subCategory = pSeries.getSubCategory();
			Category category = subCategory.getCategory();
			List<Product> products = this.productRepository.findProductsBySeries(pSeries.getPSeriesCd());
			products.stream().forEach(p -> {
				
				p.setProductSeries(null);
			});
			products = this.productRepository.saveAll(products);
			this.productSeriesRepository.delete(pSeries);
			List<ProductSeries> productSeries = this.productSeriesRepository.findProductSeriesBySubCateCd(subCategory.getSubCateCd());
			if (productSeries.size() == 0) {
				
				this.subCategoryRepository.delete(subCategory);
			}
			
			List<SubCategory> subCategories = this.subCategoryRepository.findByCategoryId(category.getCateCd());
			if (subCategories.size() == 0) {
				
				this.categoryRepository.delete(category);
			}
			
			msg.setMessageTitle("Xoá danh serie sản phẩm thành công!");
			return true;
		}

		msg.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại!");
		return false;
	}
}
