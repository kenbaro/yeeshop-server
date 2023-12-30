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
@Table(name = YeeEntityConst.USER_ADDRESS)
public class UserAddress {

	@Id @Column(name = "UAdrCd")
	private String uAdrCd;
	
	@Column(name = "Province")
	private String province;
	
	@Column(name = "District")
	private String district;
	
	@Column(name = "Commune")
	private String commune;
	
	@Column(name = "Hamlet")
	private String hamlet;
	
	@ManyToOne @JoinColumn(name = "userCd")
	private User user;
	
	@Column(name = "adrType")
	private Integer adrType;
	
	@Column(name = "wardId")
	private String wardId;
	
	@Column(name = "districtId")
	private String districtId;
	
	@Column(name = "provinceId")
	private String provinceId;
}
