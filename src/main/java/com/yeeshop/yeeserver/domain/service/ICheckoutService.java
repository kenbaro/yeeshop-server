package com.yeeshop.yeeserver.domain.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.yeeshop.yeeserver.domain.dto.checkout.CheckoutDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;

import jakarta.servlet.http.HttpServletRequest;

public interface ICheckoutService {

	public void getProvinceInfo(CheckoutDto checkoutDto) throws JsonParseException, IOException;
	
	public void getDistrictInfo(CheckoutDto checkoutDto) throws JsonParseException, IOException;
	
	public void getWardInfo(CheckoutDto checkoutDto) throws JsonParseException, IOException;
	
	public void getUserInfo(CheckoutDto checkoutDto);
	
	public void getDeliveryUnitInfo(CheckoutDto checkoutDto);
	
	public void initItem(CheckoutDto checkoutDto);
	
	public void calcFeeDelivery(CheckoutDto checkoutDto);
	
	public void getDeliveryService(CheckoutDto checkoutDto);
	
	public void calcTotalPrice(CheckoutDto checkoutDto);
	
	public void getPaymentMethod(CheckoutDto checkoutDto);
	
	public String createPayment(CheckoutDto checkoutDto, YeeMessageDto yeeMessageDto);
	
	public void updatePaymentCallBack(String orderId, HttpServletRequest request, Boolean isSuccess);
}
