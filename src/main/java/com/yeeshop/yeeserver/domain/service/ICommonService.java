package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.common.YeeCommonDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeHeaderDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.Sales;

/**
 * Interface for Common Service.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
public interface ICommonService {

	public void getHeader(YeeHeaderDto yeeHeaderDto);
	
	public YeeCommonDto editCommon(YeeHeaderDto yeeHeaderDto);
	
	public List<Banner> getListBanners();
	
	public Boolean checkSaleExpired(Sales sale);
	
	public YeeMessageDto getYeeMessage(String msgId, Integer typeMsg, YeeMessageDto yeeMessageDto);
	
}
