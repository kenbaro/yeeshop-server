package com.yeeshop.yeeserver.domain.entity;

import com.yeeshop.yeeserver.domain.constant.YeeEntityConst;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = YeeEntityConst.BRAND)
public class Brand {

	@Id @Column(name = "brandCd")
	private String brandCd;
	
	@Column(name = "brandNm")
	private String brandNm;
	
	@Column(name = "brMailContact")
	private String brMailContact;
	
	@Column(name = "brTelContact")
	private String brTelContact;

	@Column(name="brCreateTime")
	private String createdTime;
	
	@Column(name="brUpdateTime")
	private String updatedTime;
	
	@Column(name="brUpdateBy")
	private String updatedBy;
	
	@Column(name="ImgLink")
	private String imgLink;
	
}
