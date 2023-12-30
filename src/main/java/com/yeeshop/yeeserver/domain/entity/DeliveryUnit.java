package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.DELIVERY_UNIT;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = DELIVERY_UNIT)
public class DeliveryUnit {

	@Id
	@Column(name = "deliveryId")
	private String deliveryId;
	
	@Column(name = "deliveryNm")
	private String deliveryNm;
	
	@Column(name = "deliveryApi")
	private String deliveryApi;
	
	@Column(name = "contactPerson")
	private String contactPerson;
	
	@Column(name = "contactNumber")
	private String contactNumber;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "shopId")
	private String shopId;
}
