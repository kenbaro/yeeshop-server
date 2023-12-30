package com.yeeshop.yeeserver.domain.service;

import com.yeeshop.yeeserver.domain.dto.home.HomeDto;

/**
 * Interface for Home Service.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
public interface IHomeService {

	/** method : get UserNm of customer
	 * @param homeDto : Data Transfer Object for Home API.
	 * */
	public void getUserNm(HomeDto homeDto);
	
	/** method : edit Common for Home API
	 * @param homeDto : Data Transfer Object for Home API.
	 * */
	public void editCommon(HomeDto homeDto);
	
	/** method : edit Initialize for Home API
	 * @param homeDto : Data Transfer Object for Home API.
	 * */
	public void editInitDto(HomeDto homeDto);
}
