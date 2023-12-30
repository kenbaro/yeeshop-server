package com.yeeshop.yeeserver.domain.dto.home;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.common.YeeCommonDto;
import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.Brand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for Home Api.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
@ToString
public class HomeDto extends YeeCommonDto {

	/** List of Large Banner*/
	private List<Banner> largeBanners;
	
	/** List of Small Right Banner*/
	private List<Banner> smallRightBanners;
	
	/** List of Long Banner*/
	private List<Banner> longBanners;
	
	/** List of Small Right Banner*/
	private List<Banner> halfLongBanners;
	
	/** List of Brands*/
	private List<Brand> brands;
	
	/** List of Home Components*/
	private List<HomeComponentDto> homeComponentDtos;
	
}
