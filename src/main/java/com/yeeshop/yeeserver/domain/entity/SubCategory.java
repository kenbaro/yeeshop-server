package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.SUB_CATEGORY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = SUB_CATEGORY)
public class SubCategory {

	@Id
	@Column(name = "subcateCd")
	private String subCateCd;
	
	@Column(name = "subcateNm")
	private String subCateNm;
	
	@ManyToOne
	@JoinColumn(name = "cateCd")
	private Category category;
}
