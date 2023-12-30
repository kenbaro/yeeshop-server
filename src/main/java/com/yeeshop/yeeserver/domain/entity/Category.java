package com.yeeshop.yeeserver.domain.entity;


import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.CATEGORY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = CATEGORY)
public class Category {

	@Id
	@Column(name = "cateCd")
	private String cateCd;
	
	@Column(name = "cateNm")
	private String cateNm;
	
	@Column(name = "showFlg")
	private Integer showFlg;

}
