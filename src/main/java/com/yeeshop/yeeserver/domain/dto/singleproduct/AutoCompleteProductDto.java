package com.yeeshop.yeeserver.domain.dto.singleproduct;

import java.util.List;

import lombok.Data;

@Data
public class AutoCompleteProductDto {

	private	List<CateSuggestDto> cateSuggests;
	
	private List<ProductSuggestDto> productSuggests;
}
