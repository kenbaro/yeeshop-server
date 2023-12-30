package com.yeeshop.yeeserver.domain.dto.sale;


import lombok.Data;

@Data
public class SaleManageDto {

	private String saleId;
	private String saleNm;
	private String dateStart;
	private String dateEnd;
	private Integer discount;
	private Integer quantity;
	private String saleDescription;
	
	private Boolean hasBanner;
	private String bnType;
	private String bnImg;
}
