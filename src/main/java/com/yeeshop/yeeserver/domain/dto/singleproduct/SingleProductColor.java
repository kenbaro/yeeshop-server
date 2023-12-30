package com.yeeshop.yeeserver.domain.dto.singleproduct;

import lombok.Data;

@Data
public class SingleProductColor {

	private String colorCd;
	
	private String colorNm;
	
	private Boolean showFlg = false;
}
