package com.yeeshop.yeeserver.domain.dto.common;

import com.yeeshop.yeeserver.domain.entity.Banner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YeeHeaderDto {

	/** full name. */
	private String fullNm;

	/** Logo Title*/
	private String mainTitle;
	
	/** Shop Phone*/
	private String shopTel;

	/** Header Banner. */
	private Banner headerBanner;
	
	/** Cart quantity*/
	private Integer cartCnt;
}
