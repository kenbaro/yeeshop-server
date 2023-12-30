package com.yeeshop.yeeserver.domain.service;

import java.text.ParseException;
import java.util.List;

import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.order.OrderInfoDto;
import com.yeeshop.yeeserver.domain.entity.Orders;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import jakarta.servlet.http.HttpServletRequest;

public interface IOrderService {

	public void getOrderInfo(OrderInfoDto orderInfoDto, String id, YeeMessageDto yeeMessageDto);
	
	public void getAllOrder(List<OrderInfoDto> orderInfoDto);
	
	public void getNewestOrder(List<OrderInfoDto> orderInfoDto);
	
	public void getAllUnDeliveryOrder(List<OrderInfoDto> orderInfoDto);
	
	public List<OrderInfoDto> filterOrders(HttpServletRequest request) throws ParseException;
	
	public Boolean updateOrderStatus(HttpServletRequest request, YeeMessage yeeMessage,Boolean updateFlg);
	
	public Boolean deliveryOrders(HttpServletRequest request, YeeMessage yeeMessage);
}
