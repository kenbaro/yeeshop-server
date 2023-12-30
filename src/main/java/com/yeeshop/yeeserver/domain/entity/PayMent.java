package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.PAY_MENT;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = PAY_MENT)
public class PayMent {
	
	@Id @Column(name = "payId")
	private String payId;
	
	@Column(name = "payNm")
	private String payNm;
}
