package com.yeeshop.yeeserver.domain.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.rest.media.v1.MediaProcessor.Order;
import com.yeeshop.yeeserver.domain.dto.order.OrderDetailDto;
import com.yeeshop.yeeserver.domain.dto.order.OrderInfoDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.ProductInfoDto;
import com.yeeshop.yeeserver.domain.dto.statistic.DateInComeDto;
import com.yeeshop.yeeserver.domain.dto.statistic.IncomeCurrentYearDto;
import com.yeeshop.yeeserver.domain.dto.statistic.ProductAnalysisDto;
import com.yeeshop.yeeserver.domain.entity.OrderDetails;
import com.yeeshop.yeeserver.domain.entity.Orders;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.repository.IOrderDetailsRepository;
import com.yeeshop.yeeserver.domain.repository.IOrderRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.service.IStatisticService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StatisticServiceImpl implements IStatisticService{
	
	@Autowired private IOrderRepository orderRepository;
	
	@Autowired private IOrderDetailsRepository orderDetailsRepository;
	
	@Autowired private IProductRepository productRepository;

	@Override
	public HashMap<String, Object> getTotalInCome() {
		
		HashMap<String, Object> hashMap = new HashMap<>();
		
		hashMap.put("total", 0);
		hashMap.put("totalOrder", 0);
		hashMap.put("totalProduct", 0);

		List<Orders> orders = this.orderRepository.findBySuccessOrder();
		
		String month = YeeDateTimeUtils.getMonthYmdMinus(YeeDateTimeUtils.getCurrentDate());
		
		if (YeeStringUtils.isNotEmpty(month)) {
			
			orders = orders.stream().filter(e -> YeeDateTimeUtils.getMonthYmdhhmmss(e.getOrderDate()).equals(month)).collect(Collectors.toList());
			
			orders.stream().forEach(order -> {
				BigDecimal totalBigDecimal = new BigDecimal(hashMap.get("total").toString());
				BigDecimal itemTotal = new BigDecimal(YeeStringUtils.removeComma(order.getAmount()));
				hashMap.put("total", totalBigDecimal.add(itemTotal));
				
				BigDecimal totalProductBigDecimal = new BigDecimal(hashMap.get("totalProduct").toString());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order);
				
				Integer cnt = details.size();
//				for(OrderDetails detail : details) {
//					
//					cnt = cnt + Integer.parseInt(detail.getQuantity());
//				}
				
				
				BigDecimal itemTotalProduct = new BigDecimal(cnt);
				hashMap.put("totalProduct", totalProductBigDecimal.add(itemTotalProduct));
			});
			
			BigDecimal totalOrderBigDecimalAll = new BigDecimal(orders.size());
			hashMap.put("totalOrder", YeeDecimalUtils.formatDecimalWithComma(totalOrderBigDecimalAll));
			
			BigDecimal totalBigDecimalAll = new BigDecimal(hashMap.get("total").toString());
			hashMap.put("total", YeeDecimalUtils.formatDecimalWithComma(totalBigDecimalAll));
			
			BigDecimal totalProductBigDecimalAll = new BigDecimal(hashMap.get("totalProduct").toString());
			hashMap.put("totalProduct", YeeDecimalUtils.formatDecimalWithComma(totalProductBigDecimalAll));
		}
		
		return hashMap;
	}

	@Override
	public IncomeCurrentYearDto getInComeCurrentYear() {
		
		IncomeCurrentYearDto incomeCurrentYearDto = new IncomeCurrentYearDto();
		
		BigDecimal janBigDecimal = BigDecimal.ZERO;
		BigDecimal febBigDecimal = BigDecimal.ZERO;
		BigDecimal marBigDecimal = BigDecimal.ZERO;
		BigDecimal aprBigDecimal = BigDecimal.ZERO;
		BigDecimal mayBigDecimal = BigDecimal.ZERO;
		BigDecimal junBigDecimal = BigDecimal.ZERO;
		BigDecimal julyBigDecimal = BigDecimal.ZERO;
		BigDecimal augBigDecimal = BigDecimal.ZERO;
		BigDecimal septBigDecimal = BigDecimal.ZERO;
		BigDecimal octBigDecimal = BigDecimal.ZERO;
		BigDecimal novBigDecimal = BigDecimal.ZERO;
		BigDecimal decBigDecimal = BigDecimal.ZERO;
		List<Orders> orders = this.orderRepository.findBySuccessOrder();
		
		Integer currentYear = YeeDateTimeUtils.getCurrentYear();
		
		orders = orders.stream().filter(e -> YeeStringUtils.startsWith(e.getOrderDate(), currentYear.toString())).collect(Collectors.toList());

		for (Orders order :orders) {
			
			String month = YeeDateTimeUtils.getMonthYmdhhmmss(order.getOrderDate());
			
			if("01".equals(month)) {
				
				janBigDecimal = janBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("02".equals(month)) {
							
				febBigDecimal = febBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("03".equals(month)) {
				
				marBigDecimal = marBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("04".equals(month)) {
				
				aprBigDecimal = aprBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("05".equals(month)) {
				
				mayBigDecimal = mayBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			if("06".equals(month)) {
				
				junBigDecimal = junBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("07".equals(month)) {
							
				julyBigDecimal  = julyBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("08".equals(month)) {
				
				augBigDecimal = augBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("09".equals(month)) {
				
				septBigDecimal = septBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("10".equals(month)) {
				
				octBigDecimal = octBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
			
			if("11".equals(month)) {
				
				novBigDecimal = novBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}

			if("12".equals(month)) {
				
				decBigDecimal = decBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order.getAmount())));
			}
		}
		
		incomeCurrentYearDto.setJan(YeeDecimalUtils.formatDecimalWithComma(janBigDecimal));
		
		incomeCurrentYearDto.setFeb(YeeDecimalUtils.formatDecimalWithComma(febBigDecimal));
		
		incomeCurrentYearDto.setMar(YeeDecimalUtils.formatDecimalWithComma(marBigDecimal));
		
		incomeCurrentYearDto.setApr(YeeDecimalUtils.formatDecimalWithComma(aprBigDecimal));
		
		incomeCurrentYearDto.setMay(YeeDecimalUtils.formatDecimalWithComma(mayBigDecimal));
		
		incomeCurrentYearDto.setJun(YeeDecimalUtils.formatDecimalWithComma(junBigDecimal));
		
		incomeCurrentYearDto.setJuly(YeeDecimalUtils.formatDecimalWithComma(julyBigDecimal));
		
		incomeCurrentYearDto.setAug(YeeDecimalUtils.formatDecimalWithComma(augBigDecimal));
		
		incomeCurrentYearDto.setSept(YeeDecimalUtils.formatDecimalWithComma(septBigDecimal));
		
		incomeCurrentYearDto.setOct(YeeDecimalUtils.formatDecimalWithComma(octBigDecimal));
		
		incomeCurrentYearDto.setNov(YeeDecimalUtils.formatDecimalWithComma(novBigDecimal));
		
		incomeCurrentYearDto.setDec(YeeDecimalUtils.formatDecimalWithComma(decBigDecimal));
		return incomeCurrentYearDto;
		
	}

	@Override
	public List<DateInComeDto> getIncomeDate(HttpServletRequest request) throws ParseException {
		
		List<DateInComeDto> inComeDtoList = new ArrayList<>();
		
		String dateStart =request.getParameter("dateStart");
		
		String dateEnd =request.getParameter("dateEnd");
		
		String type =request.getParameter("type");
		
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("dateStart", dateStart);
		paramMap.put("dateEnd", dateEnd);
		paramMap.put("type", type);
		
		if (!YeeStringUtils.isNotEmpty(paramMap.get("dateStart")) || !YeeStringUtils.isNotEmpty(paramMap.get("dateEnd"))) {
			
			
			if ("2".equals(paramMap.get("type"))) {
				
				paramMap.put("dateStart", "1000-01");
				paramMap.put("dateEnd", "9999-12");

			} else if ("3".equals(paramMap.get("type"))) {
				
				paramMap.put("dateStart", "1000");
				paramMap.put("dateEnd", "9999");
			} else {

				paramMap.put("dateStart", "1000-01-01");
				paramMap.put("dateEnd", "9999-12-31");
			}
		}
		
		List<Orders> orders = this.orderRepository.findBySuccessOrder();
		
		orders.stream().forEach(e -> {
			
			String dateString = "";
			try {

				if ("2".equals(paramMap.get("type"))) {
					
					dateString = YeeDateTimeUtils.convertYmdhms2YmMinus(e.getOrderDate());

				} else if ("3".equals(paramMap.get("type"))) {
					
					dateString = YeeDateTimeUtils.convertYmdhms2Year(e.getOrderDate());
					
				} else {
					dateString = YeeDateTimeUtils.convertYmdhms2YmdMinus(e.getOrderDate());

				}
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.setOrderDate(dateString);
		});
		
		if (YeeStringUtils.isNotEmpty(paramMap.get("dateStart")) && YeeStringUtils.isNotEmpty(paramMap.get("dateEnd"))) {
			
			if ("2".equals(paramMap.get("type"))) {
				
				orders = orders.stream().filter(e -> YeeDateTimeUtils.calcMonthsBetween2YearMonth(paramMap.get("dateStart"), e.getOrderDate())>=0 
						&& YeeDateTimeUtils.calcMonthsBetween2YearMonth(e.getOrderDate(),paramMap.get("dateEnd"))>=0
						).collect(Collectors.toList());
			} else if ("3".equals(paramMap.get("type"))) {
				
				orders = orders.stream().filter(e -> Integer.parseInt(paramMap.get("dateStart")) <= Integer.parseInt(e.getOrderDate())
						&& Integer.parseInt(e.getOrderDate()) <= Integer.parseInt(paramMap.get("dateEnd"))
						).collect(Collectors.toList());
			} else {
				orders = orders.stream().filter(e -> YeeDateTimeUtils.calcDaysBetween2Dates(paramMap.get("dateStart"), e.getOrderDate())>=0 
						&& YeeDateTimeUtils.calcDaysBetween2Dates(e.getOrderDate(),paramMap.get("dateEnd"))>=0
						).collect(Collectors.toList());
			}
		}
			

		List<Orders> filteredList = orders.stream()
        .collect(Collectors.toMap(
        		Orders::getOrderDate,
                Function.identity(),
                (existing, replacement) -> existing
        ))
        .values()
        .stream()
        .collect(Collectors.toList());
		Collections.sort(filteredList, (obj1, obj2) -> obj1.getOrderDate().compareTo(obj2.getOrderDate()));
		
		
		
		for (Orders order : filteredList) {
			
			List<Orders> subList = orders.stream().filter(e -> YeeStringUtils.equals(order.getOrderDate(), e.getOrderDate())).collect(Collectors.toList());
			DateInComeDto inComeDto = new DateInComeDto();
			
			BigDecimal productCntBigDecimal = BigDecimal.ZERO;
			BigDecimal amountBigDecimal = BigDecimal.ZERO;
			List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
			for (Orders order2 : subList) {
				amountBigDecimal = amountBigDecimal.add(new BigDecimal(YeeStringUtils.removeComma(order2.getAmount())));
				OrderInfoDto orderInfoDto = new OrderInfoDto();
				// order info
				orderInfoDto.setOrderId(order2.getOrderId());
				orderInfoDto.setAmount(order2.getAmount());
				orderInfoDto.setOrderDate(order2.getOrderDate());
				orderInfoDto.setOrderUser(order2.getOrderUser());
				
				// pay method
				orderInfoDto.setPayMethod(order2.getPayMent().getPayNm());
				
				// order receipt info
				orderInfoDto.setDelivery(order2.getDelivery().getDeliveryNm());
				
				String statusStr = YeeStringUtils.getOrderStatusCode(order2.getStatus().toString());
				if (YeeStringUtils.isNotEmpty(statusStr)) {
					
					orderInfoDto.setOrderStatus(statusStr);	
				}
				
				orderInfoDto.setDetails(new ArrayList<>());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order2);
				
				for (OrderDetails orderDetail: details) {
					
					productCntBigDecimal = productCntBigDecimal.add(new BigDecimal(orderDetail.getQuantity()));	

					OrderDetailDto dto = new OrderDetailDto();
					dto.setNewPrice(orderDetail.getNewPrice());
					dto.setOrderQty(orderDetail.getQuantity());
					if (orderDetail.getProduct() != null) {
						dto.setProductNm(orderDetail.getProduct().getProductNm());
						dto.setProductImage(orderDetail.getProduct().getProductImage().getMainImage());
						dto.setDiscount(orderDetail.getDiscount().toString());
					}
					
					if (orderDetail.getSale() != null) {
						
						dto.setSales(orderDetail.getSale());
					}
					orderInfoDto.getDetails().add(dto);
				}
				
				orderInfoDtos.add(orderInfoDto);
			}
			

			inComeDto.setAmount(YeeDecimalUtils.formatDecimalWithComma(amountBigDecimal));
			inComeDto.setProductCnt(YeeDecimalUtils.formatDecimalWithComma(productCntBigDecimal));
			inComeDto.setDate(order.getOrderDate());
			inComeDto.setOrderCnt(String.valueOf(subList.size()));
			inComeDto.setOrders(orderInfoDtos);
			
			inComeDtoList.add(inComeDto);
			
		}
		return inComeDtoList;
	}

	@Override
	public List<ProductAnalysisDto> getProductAnalysis() {

		List<ProductAnalysisDto> productAnalysisDtos = new ArrayList<>();
		
		List<String> productNmList = this.productRepository.findAllProductNm();
		
		productNmList.forEach(e -> {
			
			ProductAnalysisDto productAnalysisDto = new ProductAnalysisDto();
			productAnalysisDto.setProductNm(e);
			List<Product> products = this.productRepository.findByProductNm(e);
			Integer viewCnt = products.stream()
	                .mapToInt(Product::getProductViewCnt)  // Convert to IntStream
	                .sum(); 
			
			productAnalysisDto.setProductView(viewCnt);
			
			List<OrderDetails> orderDetails = this.orderDetailsRepository.findByProductNm(e);
			

			Integer orderCnt = 0;
			
			for(OrderDetails orderDetail : orderDetails) {
				
				orderCnt += Integer.parseInt(orderDetail.getQuantity());
			}
			
			productAnalysisDto.setProductSold(orderCnt);
			
			productAnalysisDtos.add(productAnalysisDto);
		});
		return productAnalysisDtos;
	}

	
}
