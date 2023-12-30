package com.yeeshop.yeeserver.domain.admin.dto.productmanage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubEditProductDto {

	@JsonProperty("productId")
	private String productId;
	
	@JsonProperty("productImage")
	private String productImage;
	
	@JsonProperty("color")
	private String color;

	@JsonProperty("unitPrice")
	private String unitPrice;
	
	@JsonProperty("qty")
	private Integer qty;

	@JsonProperty("isAvailable")
	private Integer isAvailable;
	
	@JsonProperty("isAddMore")
	private Boolean isAddMore;
}
