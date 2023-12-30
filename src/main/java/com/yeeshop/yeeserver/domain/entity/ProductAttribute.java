package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.PRODUCT_ATTRIBUTE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = PRODUCT_ATTRIBUTE)
public class ProductAttribute {

	@Id @Column(name = "pAttrId")
	private String pAttrId;
	
	@Column(name = "monitorTech")
	private String monitorTech;
	
	@Column(name = "monitorWide")
	private String monitorWide;
	
	@Column(name = "touchGlassSurface")
	private String touchGlassSurface;
	
	@Column(name = "monitorResolution")
	private String monitorResolution;
	
	@Column(name = "afterResolution")
	private String afterResolution;
	
	@Column(name = "film")
	private String film;
	
	@Column(name = "beforeResolution")
	private String beforeResolution;
	
	@Column(name = "pCPU")
	private String pCPU;
	
	@Column(name = "operatingSystem")
	private String operatingSystem;
	
	@Column(name = "pGpu")
	private String pGpu;
	
	@Column(name = "internalMemory")
	private String internalMemory;
	
	@Column(name = "remainingMemory")
	private String remainingMemory;
	
	@Column(name = "externalMemory")
	private String externalMemory;
	
	@Column(name = "mobileNetwork")
	private String mobileNetwork;
	
	@Column(name = "headphoneJack")
	private String headphoneJack;
	
	@Column(name = "SIM")
	private String SIM;
	
	@Column(name = "bluetooth")
	private String bluetooth;
	
	@Column(name = "GPS")
	private String GPS;
	
	@Column(name = "Battery")
	private String Battery;
	
	@Column(name = "batteryTech")
	private String batteryTech;
	
	@Column(name = "design")
	private String design;
	
	@Column(name = "size")
	private String size;
	
	@Column(name = "length")
	private String length;
	
	@Column(name = "width")
	private String width;
	
	@Column(name = "height")
	private String height;

	@Column(name = "weight")
	private String weight;
	
	@Column(name = "material")
	private String material;
}
