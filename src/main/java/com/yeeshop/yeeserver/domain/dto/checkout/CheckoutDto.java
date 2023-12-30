package com.yeeshop.yeeserver.domain.dto.checkout;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.cart.CartItem;
import com.yeeshop.yeeserver.domain.entity.PayMent;

import lombok.Data;

@Data
public class CheckoutDto {

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
	
	private String totalPrice;
	
	private String deliveryFee;

	private String shopId;
	
	private String deliveryApi;
	
	private String deliveryToken;
	
	private List<AddressProvinceDto> provinces;
	
	private List<AddressDistrictDto> districts;
	
	private List<AddressWardDto> wards;
	
	private String provinceCd;

	private String districtCd;
	
	private String fromDistrictId;
	
	private String wardCd;
	
	private List<DeliveryUnitDto> deliveryUnits;
	
	private String deliveryUnitId;
	
	private String deliveryUnitNm;
	
    private Integer heightTotal;
    private Integer lengthTotal;
    private Integer weightTotal;
    private Integer widthTotal;
    
    private String deliveryServiceId;
    private String deliveryServiceTypeId;
    private List<DeliveryServiceDto> deliveryServices;
    
    private String payId;
    private List<PayMent> payMents;
}
