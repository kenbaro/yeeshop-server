package com.yeeshop.yeeserver.app.controller.admin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.order.OrderInfoDto;
import com.yeeshop.yeeserver.domain.entity.Orders;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.IMailService;
import com.yeeshop.yeeserver.domain.service.IOrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class OrderAdminApi {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired IMailService mailService;

	@GetMapping("/get-all-orders")
	public ResponseEntity<?> getAllOrders() {
		
		List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
		
		
		this.orderService.getAllOrder(orderInfoDtos);
		
		
		return new ResponseEntity<>(orderInfoDtos,HttpStatus.OK);
	}
	
	@GetMapping("/get-newest-orders")
	public ResponseEntity<?> getNewsOrders() {
		
		List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
		
		
		this.orderService.getNewestOrder(orderInfoDtos);

		orderInfoDtos = orderInfoDtos.stream()
				.sorted(Comparator.comparing(OrderInfoDto::getOrderDate, String::compareTo).reversed())
				.collect(Collectors.toList());
		
		return new ResponseEntity<>(orderInfoDtos,HttpStatus.OK);
	}
	
	@GetMapping("/get-order")
	public ResponseEntity<?> getOrderById(HttpServletRequest request) {
		
		OrderInfoDto orderInfoDto = new OrderInfoDto();
		YeeMessageDto yeeMessageDto = new YeeMessageDto();
		String id = request.getParameter("orderId");
		this.orderService.getOrderInfo(orderInfoDto,id,yeeMessageDto);
		
		
		return new ResponseEntity<>(orderInfoDto,HttpStatus.OK);
	}
	
	@GetMapping("/get-orders-un-delivery")
	public ResponseEntity<?> getOrdersUnDelivery(HttpServletRequest request) {
		
		List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
		
		
		this.orderService.getAllUnDeliveryOrder(orderInfoDtos);
		
		
		return new ResponseEntity<>(orderInfoDtos,HttpStatus.OK);
	}
	
	@GetMapping("/update-order-status")
	public ResponseEntity<?> updateOrderStatus(HttpServletRequest request) {
		
		YeeMessage yeeMessage = new YeeMessage();
		Boolean updateOk = this.orderService.updateOrderStatus(request,yeeMessage,true);

		if (!updateOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
	
	@GetMapping("/cancel-order")
	public ResponseEntity<?> cancelOrder(HttpServletRequest request) {
		
		YeeMessage yeeMessage = new YeeMessage();
		Boolean updateOk = this.orderService.updateOrderStatus(request,yeeMessage,false);

		if (!updateOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
	
	@GetMapping("/delivery-orders")
	public ResponseEntity<?> delverOrders(HttpServletRequest request) {
		
		YeeMessage yeeMessage = new YeeMessage();
		Boolean updateOk = this.orderService.deliveryOrders(request,yeeMessage);

		if (!updateOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(),HttpStatus.OK);
	}
	
	@GetMapping("/filter-order")
	public ResponseEntity<?> getAllSales(HttpServletRequest request) throws ParseException {

		List<OrderInfoDto> orderInfoDtos = this.orderService.filterOrders(request);
		
		return new ResponseEntity<>(orderInfoDtos, HttpStatus.OK);
	}
}
