package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeString;
import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_1;

import com.yeeshop.yeeserver.domain.dto.common.YeeCommonDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeHeaderDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.Cart;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.entity.ShopInfo;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.IBannerRepository;
import com.yeeshop.yeeserver.domain.repository.ICartRepository;
import com.yeeshop.yeeserver.domain.repository.IHeaderRepository;
import com.yeeshop.yeeserver.domain.service.ICommonService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

/**
 * Service Class For Common Service.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Service
public class CommonServiceImpl  implements ICommonService{

	@Autowired
	private IUserService userService;

	@Autowired 
	IHeaderRepository headerRepository;
	
	@Autowired
	IBannerRepository bannerRepository;
	
	@Autowired
	ICartRepository cartRepository;

	@Override
	public void getHeader(YeeHeaderDto yeeHeaderDto) {
		
		if (null == yeeHeaderDto) {
			
			yeeHeaderDto = new YeeHeaderDto();
		}

		// get validated user
		User user = this.userService.validateUser();
		
		// set init Empty fullNm
		yeeHeaderDto.setFullNm(YeeString.EMPTY);

		if (null != user) {

			if (!YeeStringUtils.isEmpty(user.getUserNm())) {
				
				yeeHeaderDto.setFullNm(user.getUserNm());
			}		
		}

		// get Information of Shop.
		ShopInfo shopInfo = this.headerRepository.getShopInfo();
		
		// set Shop Name.
		yeeHeaderDto.setMainTitle(shopInfo.getShopNm());
		
		// set Telephone.
		yeeHeaderDto.setShopTel(shopInfo.getShopTel());
		
		// set header banner
		Banner headBanner = this.headerRepository.getHeaderBanner();
		yeeHeaderDto.setHeaderBanner(headBanner);
		
		List<Cart> carts = this.cartRepository.findCartByUserCd(user.getUserCd());
		Integer cartCnt = 0;
		for (Cart c: carts) {
			cartCnt += c.getCartQuantity();
		}
		yeeHeaderDto.setCartCnt(cartCnt);

	}


	@Override
	public YeeCommonDto editCommon(YeeHeaderDto yeeHeaderDto) {
		
		YeeCommonDto yeeCommonDto =new YeeCommonDto();
		
		if (null != yeeHeaderDto) {
			
			yeeCommonDto.setYeeHeaderDto(yeeHeaderDto);
		}
		
		
		return yeeCommonDto;
	}
	
	@Override
	public List<Banner> getListBanners() {

		List<Banner> bnList = new ArrayList<>();
		
		bnList = this.bannerRepository.findAll();
		if (YeeCommonConst.YeeNumber.INT_0 > bnList.size()) {
			
			return new ArrayList<>();
		}
		
		return bnList;
	}


	@Override
	public Boolean checkSaleExpired(final Sales sale) {

		if (sale.getIsExprired() == INT_1) {
			
			return false;
		}
		
		if (sale.getIsFlashSale() == INT_1) {
			
		} else {
			
			
		}
		return true;
	}


	@Override
	public YeeMessageDto getYeeMessage(String msgId, Integer typeMsg,YeeMessageDto yeeMessageDto) {
		
		if (null == yeeMessageDto.getMessages()) {
			
			yeeMessageDto.setMessages(new ArrayList<>());
		}

		if (typeMsg == 0) { 

			yeeMessageDto.setIsError(true); 
		} else {

			yeeMessageDto.setIsError(false);
		}
		
		YeeMessage msg = new YeeMessage();

		// set Message :
		msg.setMessageTitle(msgId);
		
		msg.setMessageType(typeMsg);
		yeeMessageDto.getMessages().add(msg);
		
		return yeeMessageDto;
	}
}
