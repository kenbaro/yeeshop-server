package com.yeeshop.yeeserver.domain.entity;

import com.yeeshop.yeeserver.domain.constant.YeeEntityConst;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;

import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = YeeEntityConst.BANNER_TABLE)
public class Banner {

	@Id @Column(name = "BnCd")
	private String BannerCd;
	
	@Column(name = "BnNm")
	private String bannerNm;
	
	@Column(name = "BnImg")
	private String bannerImg;
	
	@Column(name = "BnLink")
	private String bannerLink;
	
	@OneToOne @JoinColumn(name = "BnTypeCd")
	private BannerType bannerType;
	
	@Column(name = "BnCreateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private String createdTimestamp;
	
	@Column(name = "BnUpdateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private String updateTimestamp;
	
	@Column(name = "BnUpdateBy")
	private String updateBy;
	
	@Column(name = "showFlg")
	private Integer showFlg;
}
