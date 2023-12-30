package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.SALES;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = SALES)
public class Sales {

	@Id
	@Column(name = "saleId")
	private String saleId;
	
	@Column(name = "saleNm")
	private String saleNm;
	
	@Column(name = "saleDescription")
	private String saleDescription;
	
	@Column(name = "fromPrice")
	private Double fromPrice;
	
	@Column(name = "disCount")
	@Min(0) @Max(100)
	private Integer disCount;
	
	@Column(name = "maxDisCount")
	private Double maxDisCount;
	
	@Column(name = "timeStart")
	private String timeStart;
	
	@Column(name = "timeEnd")
	private String timeEnd;
	
	@Column(name = "dayStart")
	private String dayStart;
	
	@Column(name = "dayEnd")
	private String dayEnd;
	
	@Column(name = "voucherNm")
	private String voucherNm;
	
	@Column(name = "isExprired")
	private Integer isExprired;
	
	@Column(name = "isFlashSale")
	private Integer isFlashSale;

	@Column(name = "saleType")
	private Integer saleType;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "maxQtyPerCus")
	private Integer maxQtyPerCus;
}
