package com.yeeshop.yeeserver.domain.dto.home;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.common.ProductCartDto;

import lombok.Data;

/**
 * Data Transfer Object for HomeComponent.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
public class HomeComponentDto {

	/** Code of Component*/
	private String componentCd;
	
	/** Title of component*/
	private String componentTitle;
	
	/** List of series of product*/
	private List<HomePrdSeriesDto> homeProductSeries;
	
	private List<ProductCartDto> products;
}
