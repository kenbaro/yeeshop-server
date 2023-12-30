package com.yeeshop.yeeserver.domain.entity;

import static com.yeeshop.yeeserver.domain.constant.YeeEntityConst.ORDERS;

import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = ORDERS)
public class Orders {

	@Id @Column(name = "orderId")
	private String orderId;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:MM:ss")
    @Column(name = "orderDate")
	private String orderDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:MM:ss")
    @Column(name = "orderReceipt")
	private String orderReceipt;
    
    @Column(name = "receptNm")
    private String receptNm;
    
    @Column(name = "orderUser")
    private String orderUser;
    
    @Column(name = "userMail")
    private String userMail;
    
    @Column(name = "tel")
    private String tel;
    
    @Column(name = "amount")
    private String amount;
    
    @Column(name = "shipFee")
    private String shipFee;
    
    @Column(name = "orderDspt")
    private String orderDspt;
    
    @ManyToOne
    @JoinColumn(name = "paymentId")
    private PayMent payMent;
    
    @ManyToOne
    @JoinColumn(name = "deliveryId")
    private DeliveryUnit delivery;
    
	@Column(name = "deliveryCode")
	private String deliveryCode;
    
	@Column(name = "wardId")
	private Integer wardId;
	
	@Column(name = "districtId")
	private Integer districtId;
	
	@Column(name = "provinceId")
	private Integer provinceId;
    
	@Column(name = "detailAddress")
	private String detailAddress;
	
	@Column(name = "provinceNm")
	private String provinceNm;
	
	@Column(name = "districtNm")
	private String districtNm;
	
	@Column(name = "wardNm")
	private String wardNm;
	
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "order" , fetch = FetchType.LAZY)
    List<OrderDetails> orderDetails;
    
    @Column(name = "status")
	private Integer status;
    
	@Column(name = "TransactionNo")
	private String TransactionNo;
	
	@Column(name = "transStatus")
	private String transStatus;
}
