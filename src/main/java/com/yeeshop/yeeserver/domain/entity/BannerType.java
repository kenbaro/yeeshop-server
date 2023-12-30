package com.yeeshop.yeeserver.domain.entity;

import com.yeeshop.yeeserver.domain.constant.YeeEntityConst;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = YeeEntityConst.BANNER_TYPE_TABLE)
public class BannerType {

	@Id @Column(name = "BnTypeCd")
	private String BnTypeCd;
	
	@Column(name = "BnTypeNm")
	private String bnTypeNm;
}
