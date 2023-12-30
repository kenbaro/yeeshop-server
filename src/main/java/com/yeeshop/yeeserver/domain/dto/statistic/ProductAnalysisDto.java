package com.yeeshop.yeeserver.domain.dto.statistic;

import lombok.Data;

@Data
public class ProductAnalysisDto {

	private String productNm;
	
	private Integer productSold;
	
	private Integer productView;
}
