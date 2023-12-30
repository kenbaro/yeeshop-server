package com.yeeshop.yeeserver.domain.entity;

import com.yeeshop.yeeserver.domain.constant.YeeEntityConst;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = YeeEntityConst.SHOP_INFO)
public class ShopInfo {

	@Id
	@Column(name = "ShopCd")
	private String shopCd;
	
	@Column(name = "ShopNm")
	private String shopNm;
	
	@Column(name = "ShopTel")
	private String shopTel;
	
	@Column(name = "ShopMail")
	private String shopMail;
	
	@Column(name = "WkStartDate")
	private String wkStartDate;
	
	@Column(name = "WkEndDate")
	private String wkEndDate;
	
	@Column(name = "WkStartTime")
	private String wkStartTime;
	
	@Column(name = "WkEndTime")
	private String wkEndTime;
	
	@Column(name="BnCreateTime")
	private String createdTime;
	
	@Column(name="BnUpdateTime")
	private String updatedTime;
	
	@Column(name="BnUpdateBy")
	private String updatedBy;
	
	@ManyToOne @JoinColumn(name = "ShopAddrId")
	UserAddress shopAddress;
}
