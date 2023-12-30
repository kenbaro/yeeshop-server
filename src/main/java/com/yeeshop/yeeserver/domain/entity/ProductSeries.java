package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.PRODUCT_SERIES;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = PRODUCT_SERIES)
public class ProductSeries {

	@Id
	@Column(name = "prdsrCd")
	private String pSeriesCd;
	
	@Column(name = "prdsrNm")
	private String pSeriesNm;
	
	@ManyToOne
	@JoinColumn(name = "subcateCd")
	private SubCategory subCategory;
}
