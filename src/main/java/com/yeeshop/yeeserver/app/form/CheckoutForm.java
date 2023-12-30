package com.yeeshop.yeeserver.app.form;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.cart.CartItem;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;
import com.yeeshop.yeeserver.domain.dto.checkout.DeliveryServiceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.DeliveryUnitDto;
import com.yeeshop.yeeserver.domain.entity.PayMent;

import lombok.Data;

@Data
public class CheckoutForm {
	
	private String userNm;
	
	private String userEmail;
	
	private String userTel;
	
	private String fullNm;
	
	private String email;
	
	private String phone;
	
	private String detailAddress;
	
	private String note;
	
	private List<CartItem> cartItems;
	
	private String tempPriceTotal;
	
	private String deliveryFee;
	
	private List<AddressProvinceDto> provinces;
	
	private List<AddressDistrictDto> districts;
	
	private List<AddressWardDto> wards;
	
	private String provinceCd;

	private String districtCd;
	
	private String wardCd;
	
	private List<DeliveryUnitDto> deliveryUnits;
	
	private String deliveryUnitId;
	
	private String deliveryUnitNm;
	
	private String deliveryServiceId;
    private List<DeliveryServiceDto> deliveryServices;
    
	private String totalPrice;
	
	private String payId;
    private List<PayMent> payMents;
	
}
