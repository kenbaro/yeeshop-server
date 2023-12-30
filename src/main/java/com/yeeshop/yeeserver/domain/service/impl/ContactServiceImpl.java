package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.dto.contact.ContactDto;
import com.yeeshop.yeeserver.domain.entity.ShopInfo;
import com.yeeshop.yeeserver.domain.entity.UserAddress;
import com.yeeshop.yeeserver.domain.repository.IHeaderRepository;
import com.yeeshop.yeeserver.domain.service.IContactService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

/**
 * Service Class For Common Api.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Service
public class ContactServiceImpl implements IContactService{

	@Autowired
	IHeaderRepository headerRepository;

	@Override
	public void getShopContact(final ContactDto contactDto) {
		
		ShopInfo shopInfo = this.headerRepository.getShopInfo();
		
		// set Shop Mail.
		contactDto.setEmail(shopInfo.getShopMail());
		
		// set Tel
		contactDto.setShopTel(shopInfo.getShopTel());
		
		List<String> addressList = new ArrayList<>();
		UserAddress userAddress = shopInfo.getShopAddress();
		
		// set address to List
		addressList.add(userAddress.getHamlet());
		addressList.add(userAddress.getDistrict());
		addressList.add(userAddress.getCommune());
		addressList.add(userAddress.getProvince());
		
		// set Address
		String address = YeeStringUtils.yeeJoinStringByCharacter(YeeCommonConst.YeeString.SEPARATE_COMMA, addressList);
		contactDto.setAddress(address);
		
		String timeWk = this.getTimeWorking(shopInfo);
		
		if (YeeStringUtils.isNotEmpty(timeWk)) {
			
			contactDto.setWorkTime(timeWk);
		}
			
	}
	
	private String getTimeWorking(final ShopInfo shopInfo) {
		
		// get Date String
		List<String> twList = new ArrayList<>();
		twList.add(shopInfo.getWkStartDate());
		twList.add(shopInfo.getWkEndDate());
		
		String date = YeeStringUtils.yeeJoinStringByCharacter(YeeCommonConst.YeeString.SEPARATE_DASH, twList);
		
		twList.clear();

		// get Time String
		twList.add(shopInfo.getWkStartTime());
		twList.add(shopInfo.getWkEndTime());
		
		String time = YeeStringUtils.yeeJoinStringByCharacter(YeeCommonConst.YeeString.SEPARATE_DASH, twList);
		
		twList.clear();
		twList.add(date);
		twList.add(time);

		return YeeStringUtils.yeeJoinStringByCharacter(YeeCommonConst.YeeString.SEPARATE_SPACE, twList);
	}

	
}
