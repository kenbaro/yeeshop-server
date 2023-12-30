package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.PRODUCT_IMAGE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = PRODUCT_IMAGE)
public class ProductImage {

	@Id @Column(name = "imageId")
	private String imageId;
	
	@Column(name = "mainImage")
	private String mainImage;
	
	@Column(name = "image1")
	private String image1;
	
	@Column(name = "image2")
	private String image2;
	
	@Column(name = "image3")
	private String image3;

}
