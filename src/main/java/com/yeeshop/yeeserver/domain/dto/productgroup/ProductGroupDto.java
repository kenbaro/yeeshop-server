package com.yeeshop.yeeserver.domain.dto.productgroup;

import java.util.List;

import com.yeeshop.yeeserver.domain.admin.dto.productmanage.CategoryDto;

import lombok.Data;

@Data
public class ProductGroupDto {

	private String pSeriesId;
	
	private String pSeriesNm;
	
	private String pSubCateId;
	
	private String pSubCateNm;
	
	private String pCateId;
	
	private String pCateNm;
	
	private List<CategoryDto> mainCates;
	
	private List<CategoryDto> subCates;
	
	private List<CategoryDto> series;
	
	private Long subCateRowSpan;
	
	private Long cateRowSpan;
}
