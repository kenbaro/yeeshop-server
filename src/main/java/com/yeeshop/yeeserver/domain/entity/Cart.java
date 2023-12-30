package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.CART;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = CART)
public class Cart {

	@Id @Column(name = "cartId")
	private String cartId;
	
	@OneToOne
	@JoinColumn(name = "cartUser")
	private User cartUser;
	
	@OneToOne
	@JoinColumn(name = "SKU")
	private Product cartProduct;
	
	@Column(name = "cartQuantity")
	private Integer cartQuantity;
	
	@Column(name = "price")
	private String cartPrice;
}
