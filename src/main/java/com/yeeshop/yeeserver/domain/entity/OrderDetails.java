package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.ORDER_DETAILS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = ORDER_DETAILS)
public class OrderDetails {

	@Id @Column(name = "oDetailsId")
	private String oDetailsId;
	
	@ManyToOne
	@JoinColumn(name = "orderId")
	private Orders order;
	
	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "saleId")
	private Sales sale;
	
	@Column(name = "unitPrice")
    private String unitPrice;
	
	@Column(name = "newPrice")
    private String newPrice;
	
	@Column(name = "quantity")
	 private String quantity;
	
	@Column(name = "discount")
	private Integer discount;
}
