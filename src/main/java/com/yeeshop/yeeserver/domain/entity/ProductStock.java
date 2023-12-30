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
@Table(name = YeeEntityConst.PRODUCT_STOCK)
public class ProductStock {

	@Id @Column(name = "psId") 
	private String psId;
	
	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;
	
	@Column(name="unitPrice")
	private String unitPrice;
	
	@Column(name="quantity")
	private String quantity;
	
    @Column(name="updatedBy")
    private String updatedBy;
    
    @Column(name="updatedTime")
    private String updatedTime;
}
