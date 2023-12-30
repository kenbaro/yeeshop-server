package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.PRODUCT;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = PRODUCT)
public class Product {

	@Id
	@Column(name = "SKU")
	private String SKU;
	
	@Column(name = "productNm")
	private String productNm;
	
	@Column(name = "productQuantity")
	private Integer productQuantity;
	
	@Column(name = "productViewCnt")
	private Integer productViewCnt;
	
	@Column(name = "productStorage")
	private String productStorage;
	
	@Column(name = "productDescription")
	private String productDescription;
	
	@Column(name = "productColor")
	private String productColor;
	
	@Column(name = "productImageColor")
	private String productImageColor;
	
	@OneToOne
	@JoinColumn(name="saleId")
	private Sales sale;
	
	@OneToOne
	@JoinColumn(name="pAttrId")
	private ProductAttribute productAttribute;
	
	@OneToOne
	@JoinColumn(name="imageId")
	private ProductImage productImage;
	
	@OneToOne
	@JoinColumn(name="serieId")
	private ProductSeries productSeries;
	
	@Column(name = "UnitPrice")
	private String unitPrice;
	
	@Column(name = "isAvailabled")
	private Integer isAvailabled;
	
	@Column(name = "pState")
	private Integer pState;
	
	@Column(name = "soldQty")
	private Integer soldQty;
	
	@ManyToOne
	@JoinColumn(name="brandCd")
	private Brand brand;
	
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateStart")
	private String dateStart;
}
