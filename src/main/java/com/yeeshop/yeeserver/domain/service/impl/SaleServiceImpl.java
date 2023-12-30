package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeeshop.yeeserver.domain.dto.sale.SaleManageDto;
import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.BannerType;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.IBannerRepository;
import com.yeeshop.yeeserver.domain.repository.IBannerTypeRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.repository.ISalesRepository;
import com.yeeshop.yeeserver.domain.service.ISaleService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class SaleServiceImpl implements ISaleService{

	@Autowired private ISalesRepository salesRepository;
	
	@Autowired private IBannerRepository bannerRepository;
	
	@Autowired private IBannerTypeRepository bannerTypeRepository;
	
	@Autowired private IProductRepository productRepository;

	@Override
	public List<Sales> getAllSalesExcept(HttpServletRequest request) {
		
		String saleId = request.getParameter("saleId");
		String date  =  request.getParameter("date");
		
		this.reloadSaleBeforeGet();
		List<Sales> sales = this.salesRepository.findSalesNotExpired();
		
		sales = sales.stream().filter(e -> !YeeStringUtils.equals(saleId, e.getSaleId()) 
				&& YeeDateTimeUtils.calcDaysBetween2Dates(e.getDayStart(), date) < 0).collect(Collectors.toList());
		return sales;
	}

	@Override
	public List<Sales> getAllSales() {
		
		this.reloadSaleBeforeGet();
		List<Sales> sales = this.salesRepository.findAll();
		
		return sales;
		
	}
	
	private void reloadSaleBeforeGet() {
		
		List<Sales> sales = this.salesRepository.findAll();
		
		sales.stream().forEach(e -> {
			
			String crrDate = YeeDateTimeUtils.getCurrentDate();
			Long dayCnt = YeeDateTimeUtils.calcDaysBetween2Dates(e.getDayEnd(), crrDate);
			Long dayCnt2 = YeeDateTimeUtils.calcDaysBetween2Dates(crrDate, e.getDayStart());
			if (dayCnt > 0) {
				
				e.setIsExprired(1);
			}
			
			if (dayCnt2 > 0) {
				
				e.setIsExprired(3);
			}
			
			// onSale and quantity sold= 0, set status ""
			if (dayCnt <= 0 && dayCnt2 <=0 && e.getQuantity() == 0) {
				
				e.setIsExprired(2);
			}
		});
		
		sales= this.salesRepository.saveAll(sales);
	}

	@Override
	public List<Sales> filterSales(HttpServletRequest request) {
		
		String title = request.getParameter("title");
		String status = request.getParameter("status");
		
		String dateStart = request.getParameter("dateStart");
		
		String dateEnd = request.getParameter("dateEnd");
		
		List<Sales> sales = new ArrayList<>();
		if(YeeStringUtils.isEmpty(title)) {
			
			sales = this.salesRepository.findAll();
		} else {
			
			sales = this.salesRepository.findSalesContainingKeyWord(title.trim());
		}
		
		if (sales.size() > 0  && YeeStringUtils.isNumbericChk(status) && !YeeStringUtils.equals("-1", status)) {
			
			sales = sales.stream().filter(e-> status.equals(e.getIsExprired().toString())).collect(Collectors.toList());
		}
		
		if (YeeDateTimeUtils.validateDateMinus(dateStart)) {
			
			sales = sales.stream().filter(e-> YeeDateTimeUtils.calcDaysBetween2Dates(dateStart,e.getDayStart()) >= 0 ).collect(Collectors.toList());
		}
		
		if (YeeDateTimeUtils.validateDateMinus(dateEnd)) {
			
			sales = sales.stream().filter(e-> YeeDateTimeUtils.calcDaysBetween2Dates(e.getDayEnd(),dateEnd) >= 0).collect(Collectors.toList());
		}
		
		return sales;
	}

	@Override
	public Boolean addSales(SaleManageDto saleManageDto, YeeMessage yeeMessage) {
		
		yeeMessage.setMessageTitle(YeeStringUtils.EMPTY);
		String currenDate = YeeDateTimeUtils.getCurrentDate();

		if (YeeStringUtils.isAllEmpty(saleManageDto.getSaleNm(),saleManageDto.getSaleDescription())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		if (!YeeDateTimeUtils.validateDateMinus(saleManageDto.getDateStart()) || !YeeDateTimeUtils.validateDateMinus(saleManageDto.getDateEnd())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		
		
		if (YeeDateTimeUtils.calcDaysBetween2Dates(saleManageDto.getDateStart(), saleManageDto.getDateEnd()) < 0) {
			
			yeeMessage.setMessageTitle("Ngày bắt đầu khuyến mãi trước ngày kết thúc khuyến mãi!");
			return false;
		}
		
		if (YeeDateTimeUtils.calcDaysBetween2Dates(saleManageDto.getDateEnd(), currenDate) > 0) {
			
			yeeMessage.setMessageTitle("Ngày kết thúc khuyến mãi không được xảy ra sau ngày hiện tại!");
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(saleManageDto.getQuantity().toString()) || !YeeStringUtils.isNumbericChk(saleManageDto.getDiscount().toString())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		Sales sales = new Sales();
		
		String currenDateTime = YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(YeeDateTimeUtils.getCurrentDateTime());
		
		sales.setSaleId("SALE"+currenDateTime);
		
		sales.setSaleNm(saleManageDto.getSaleNm());
		sales.setSaleDescription(saleManageDto.getSaleDescription());
		sales.setDayStart(saleManageDto.getDateStart());
		sales.setDayEnd(saleManageDto.getDateEnd());
		sales.setDisCount(saleManageDto.getDiscount());
		sales.setQuantity(saleManageDto.getQuantity());
		
		Long calcFromCrr = YeeDateTimeUtils.calcDaysBetween2Dates(currenDate,sales.getDayStart());
		
		if (calcFromCrr > 0) {
			
			// Sắp diễn ra
			sales.setIsExprired(3);
		} else {
			
			// Đang diễn ra
			sales.setIsExprired(0);
		}
		
		sales.setSaleType(1);
		
		sales = this.salesRepository.save(sales);
		
		if (null == sales) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		// banner
		if (saleManageDto.getHasBanner() && !YeeStringUtils.isAllEmpty(saleManageDto.getBnType(),saleManageDto.getBnImg())) {
			
			Banner banner = new Banner();
			
			Optional<BannerType> bntOptional = this.bannerTypeRepository.findById(saleManageDto.getBnType());
			BannerType bannerType = bntOptional.isPresent() ? bntOptional.get() : null;
			
			if (null == bannerType) {
				
				yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			banner.setBannerType(bannerType);
			banner.setBannerCd("BN"+currenDateTime);
			banner.setBannerNm(bannerType.getBnTypeNm());
			banner.setShowFlg(1);
			banner.setBannerImg(saleManageDto.getBnImg());
			banner.setBannerLink(sales.getSaleId());
			
			banner = bannerRepository.save(banner);
			
			if (null == banner) {
				
				yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
		}
		
		yeeMessage.setMessageTitle("Thêm chương trình khuyến mãi thành công!");
		return true;
	}

	@Override
	public void getSaleById(SaleManageDto saleManageDto, HttpServletRequest request) {

		String saleId = request.getParameter("saleId");
		
		Optional<Sales> saleOptional = this.salesRepository.findById(saleId);
		
		Sales sale = saleOptional.isPresent() ? saleOptional.get() : null; 

		if (null != sale) {
			
			saleManageDto.setSaleId(sale.getSaleId());
			saleManageDto.setSaleNm(sale.getSaleNm());
			saleManageDto.setSaleDescription(sale.getSaleDescription());
			saleManageDto.setDiscount(sale.getDisCount());
			saleManageDto.setQuantity(sale.getQuantity());
			saleManageDto.setDateStart(sale.getDayStart());
			saleManageDto.setDateEnd(sale.getDayEnd());
			
			List<Banner> banners = this.bannerRepository.findByBannerLink(sale.getSaleId());
			saleManageDto.setBnImg(YeeStringUtils.EMPTY);
			saleManageDto.setBnType(YeeStringUtils.EMPTY);
			saleManageDto.setHasBanner(false);

			if (banners.size() > 0) {
				
				saleManageDto.setBnImg(banners.get(0).getBannerImg());
				saleManageDto.setBnType(banners.get(0).getBannerType().getBnTypeCd());
				saleManageDto.setHasBanner(true);
			}
		}
		
	}

	@Override
	public Boolean updateSales(SaleManageDto saleManageDto, YeeMessage yeeMessage) {
		
		yeeMessage.setMessageTitle(YeeStringUtils.EMPTY);
		String currenDate = YeeDateTimeUtils.getCurrentDate();

		if (YeeStringUtils.isAllEmpty(saleManageDto.getSaleNm(),saleManageDto.getSaleDescription())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		if (!YeeDateTimeUtils.validateDateMinus(saleManageDto.getDateStart()) || !YeeDateTimeUtils.validateDateMinus(saleManageDto.getDateEnd())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		
		
		if (YeeDateTimeUtils.calcDaysBetween2Dates(saleManageDto.getDateStart(), saleManageDto.getDateEnd()) < 0) {
			
			yeeMessage.setMessageTitle("Ngày bắt đầu khuyến mãi trước ngày kết thúc khuyến mãi!");
			return false;
		}
		
		if (YeeDateTimeUtils.calcDaysBetween2Dates(saleManageDto.getDateEnd(), currenDate) > 0) {
			
			yeeMessage.setMessageTitle("Ngày kết thúc khuyến mãi không được xảy ra sau ngày hiện tại!");
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(saleManageDto.getQuantity().toString()) || !YeeStringUtils.isNumbericChk(saleManageDto.getDiscount().toString())) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		Optional<Sales> saleOptional = this.salesRepository.findById(saleManageDto.getSaleId());
		
		Sales sales = saleOptional.isPresent() ? saleOptional.get() : null;
		
		if (null == sales) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}

		String currenDateTime = YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(YeeDateTimeUtils.getCurrentDateTime());
		
		sales.setSaleNm(saleManageDto.getSaleNm());
		sales.setSaleDescription(saleManageDto.getSaleDescription());
		sales.setDayStart(saleManageDto.getDateStart());
		sales.setDayEnd(saleManageDto.getDateEnd());
		sales.setDisCount(saleManageDto.getDiscount());
		sales.setQuantity(saleManageDto.getQuantity());
		
		Long calcFromCrr = YeeDateTimeUtils.calcDaysBetween2Dates(currenDate,sales.getDayStart());
		
		if (calcFromCrr > 0) {
			
			// Sắp diễn ra
			sales.setIsExprired(3);
		} else {
			
			// Đang diễn ra
			sales.setIsExprired(0);
		}
		
		sales.setSaleType(1);
		
		sales = this.salesRepository.save(sales);
		
		if (null == sales) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		// banner
		List<Banner> banners = this.bannerRepository.findByBannerLink(saleManageDto.getSaleId());;
		
		Banner banner = new Banner();
		if (banners.size() > 0) {
			
			banner = banners.get(0);
		}
		
		if (!saleManageDto.getHasBanner() && YeeStringUtils.isNotEmpty(banner.getBannerCd())) {
			
			this.bannerRepository.delete(banner);
		} else if (saleManageDto.getHasBanner() && !YeeStringUtils.isAllEmpty(saleManageDto.getBnType(),saleManageDto.getBnImg())) {
			
			Optional<BannerType> bntOptional = this.bannerTypeRepository.findById(saleManageDto.getBnType());
			BannerType bannerType = bntOptional.isPresent() ? bntOptional.get() : null;
			
			if (null == bannerType) {
				
				yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
			
			banner.setBannerType(bannerType);
			
			if (YeeStringUtils.isEmpty(banner.getBannerCd())) {
				
				banner.setBannerCd("BN"+currenDateTime);
			}
			banner.setBannerNm(bannerType.getBnTypeNm());
			banner.setShowFlg(1);
			banner.setBannerImg(saleManageDto.getBnImg());
			banner.setBannerLink(sales.getSaleId());
			
			banner = bannerRepository.save(banner);
			
			if (null == banner) {
				
				yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
				return false;
			}
		}
		
		yeeMessage.setMessageTitle("Cập nhật chương trình khuyến mãi thành công!");
		return true;
	}

	@Override
	public Boolean updateStatus(HttpServletRequest request, YeeMessage yeeMessage) {
		
		String id = request.getParameter("saleId");
		
		Optional<Sales> saleOptional = this.salesRepository.findById(id);
		
		Sales sales = saleOptional.isPresent() ? saleOptional.get() : null;
		
		if (null == sales) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		if (sales.getIsExprired()!= 2 && sales.getIsExprired() != 0) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		if (sales.getIsExprired() == 2) {
			
			sales.setIsExprired(0);
			sales = this.salesRepository.save(sales);
		} else if (sales.getIsExprired() == 0) {
			sales.setIsExprired(2);
			sales = this.salesRepository.save(sales);
		}

		yeeMessage.setMessageTitle("Cập nhật trạng thái chương trình khuyến mãi thành công!");
		return true;
	}

	@Override
	public Boolean deleteSale(HttpServletRequest request, YeeMessage yeeMessage) {
		
		String id = request.getParameter("saleId");
		
		Optional<Sales> saleOptional = this.salesRepository.findById(id);
		
		Sales sales = saleOptional.isPresent() ? saleOptional.get() : null;
		
		if (null == sales) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình thực thi, vui lòng thử lại sau!");
			return false;
		}
		
		List<Product> products = this.productRepository.findBySale(sales);
		
		products.stream().forEach(product -> {
			
			product.setSale(null);
		});

		products = this.productRepository.saveAll(products);
		this.salesRepository.delete(sales);

		yeeMessage.setMessageTitle("Xoá chương trình khuyến mãi thành công!");
		return true;
	}
}
