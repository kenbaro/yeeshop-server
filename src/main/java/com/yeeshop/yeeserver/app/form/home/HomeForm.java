package com.yeeshop.yeeserver.app.form.home;

import java.util.List;

import com.yeeshop.yeeserver.app.form.common.YeeCommonForm;
import com.yeeshop.yeeserver.domain.dto.home.HomeComponentDto;
import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.Brand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HomeForm extends YeeCommonForm{
	
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
