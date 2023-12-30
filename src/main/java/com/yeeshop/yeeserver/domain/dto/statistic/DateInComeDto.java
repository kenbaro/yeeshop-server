package com.yeeshop.yeeserver.domain.dto.statistic;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.order.OrderInfoDto;
import com.yeeshop.yeeserver.domain.dto.singleproduct.ProductInfoDto;

import lombok.Data;

@Data
public class DateInComeDto {

	private String date;
	
	private String orderCnt;
	
	private String productCnt;
	
	private String amount;
	
	private List<ProductInfoDto> orderedProducts;
	
	private List<OrderInfoDto> orders;
	
}
