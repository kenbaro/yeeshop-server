package com.yeeshop.yeeserver.domain.admin.dto.productmanage;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yeeshop.yeeserver.domain.dto.common.ProductImageDto;
import com.yeeshop.yeeserver.domain.entity.Sales;

import lombok.Data;

@Data
public class EditProductDto {

	private String productNm;
	
	private String mainCateCd;
	private List<CategoryDto> mainCategories;
	
	private String subCateCd;
	private List<CategoryDto> subCategories;
	
	private String brandCd;
	private List<CategoryDto> brands;
	
	private String serieCd;
	private List<CategoryDto> series;
	
	private String storage;
	
	private List<String> storages;
	
	private String productDspt;
	
	private List<String> colors;
	
	private EditProductAttrDto productAttribute;
	
	private ProductImageDto productSubImageDto;
	
	@JsonProperty("subEditProducts")
	private List<SubEditProductDto> subEditProducts;
	
	private String dateStart;
	
	private String tempDate;
	
	private Boolean isSale = false;
	
	private Sales sales;
	
	private String message;
	
	private String pAttrId;
	
	private String monitorTech;
	

	private String monitorWide;
	

	private String touchGlassSurface;
	

	private String monitorResolution;
	

	private String afterResolution;
	

	private String film;
	

	private String beforeResolution;
	

	private String pCPU;
	

	private String operatingSystem;
	
	private String pGpu;
	

	private String internalMemory;
	
	private String externalMemory;

	private String remainingMemory;
	
	private String mobileNetwork;
	
	private String headphoneJack;
	
	private String SIM;
	
	private String bluetooth;
	
	private String GPS;
	
	private String Battery;

	private String batteryTech;
	

	private String design;
	

	private String size;
	

	private String length;
	

	private String width;
	

	private String height;


	private String weight;
	
	private String material;
	
	
}
