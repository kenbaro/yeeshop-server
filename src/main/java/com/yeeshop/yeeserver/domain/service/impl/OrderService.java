package com.yeeshop.yeeserver.domain.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twilio.rest.media.v1.MediaProcessor.Order;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.dto.checkout.CheckoutDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.order.OrderDetailDto;
import com.yeeshop.yeeserver.domain.dto.order.OrderInfoDto;
import com.yeeshop.yeeserver.domain.entity.DeliveryUnit;
import com.yeeshop.yeeserver.domain.entity.OrderDetails;
import com.yeeshop.yeeserver.domain.entity.Orders;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.entity.ShopInfo;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.model.deliveryservice.DeliveryService;
import com.yeeshop.yeeserver.domain.model.deliveryservice.ServiceData;
import com.yeeshop.yeeserver.domain.model.deliveryservice.ServiceRequestBody;
import com.yeeshop.yeeserver.domain.model.fee.DeliveryFee;
import com.yeeshop.yeeserver.domain.model.fee.FeeRequestBody;
import com.yeeshop.yeeserver.domain.repository.IDeliveryUnitRepository;
import com.yeeshop.yeeserver.domain.repository.IHeaderRepository;
import com.yeeshop.yeeserver.domain.repository.IOrderDetailsRepository;
import com.yeeshop.yeeserver.domain.repository.IOrderRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.repository.IUserRepository;
import com.yeeshop.yeeserver.domain.service.IOrderService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeSendMailUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderService implements IOrderService{
	
	@Autowired 
	private IOrderRepository orderRepository;
	
	@Autowired IOrderDetailsRepository orderDetailsRepository;
	
	@Autowired IUserService userService;
	
	@Autowired private YeeSendMailUtils sendMailUtils;
	
	@Autowired private IDeliveryUnitRepository deliveryUnitRepository;
	
	@Autowired private IHeaderRepository headerRepository;
	
	@Autowired private IProductRepository productRepository;
	
//	@Autowired
//	private IOrderDetailsRepository orderDetailsRepository;
	@Override
	public void getOrderInfo(final OrderInfoDto orderInfoDto, final String id, final YeeMessageDto yeeMessageDto) {
		
		yeeMessageDto.setMessages(new ArrayList<>());
		
		yeeMessageDto.setIsError(false);
		if (YeeStringUtils.isEmpty(id)) {
			
			yeeMessageDto.setIsError(true);
		} else {
			
			Optional<Orders> orderOptional = this.orderRepository.findById(id);
			
			Orders order = orderOptional.isPresent() ? orderOptional.get() : null;
			
			if (null == order) {
				
				yeeMessageDto.setIsError(true);
			} else {
				
				orderInfoDto.setOrderId(order.getOrderId());
				orderInfoDto.setAmount(order.getAmount());
				orderInfoDto.setOrderDate(order.getOrderDate());
				orderInfoDto.setOrderRPeople(order.getReceptNm());
				orderInfoDto.setRPeopleTel(order.getTel());
				
				orderInfoDto.setShipFee(order.getShipFee());
				orderInfoDto.setOrderDspt( null == order.getOrderDspt() ? YeeStringUtils.EMPTY : order.getOrderDspt());
				
				orderInfoDto.setPayMethod(order.getPayMent().getPayNm());
				orderInfoDto.setPayType(order.getPayMent().getPayId().equals("PAY01") ? 0 : 1);
				orderInfoDto.setDelivery(order.getDelivery().getDeliveryNm());
				
				orderInfoDto.setDeliveryCode(null == order.getDeliveryCode() ? YeeStringUtils.EMPTY : order.getDeliveryCode());
				
				orderInfoDto.setOrderUser(order.getOrderUser());
				orderInfoDto.setUserMail(order.getUserMail());
				

				orderInfoDto.setDetails(new ArrayList<>());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order);
				for(OrderDetails orderDetail : details) {
					
					OrderDetailDto dto = new OrderDetailDto();
					
					dto.setNewPrice(orderDetail.getNewPrice());
					
					
					dto.setOrderQty(orderDetail.getQuantity());
					
					if (null != orderDetail.getProduct()) {
						
						dto.setProductColor(orderDetail.getProduct().getProductColor());
						dto.setProductNm(orderDetail.getProduct().getProductNm());
						dto.setProductStorage(orderDetail.getProduct().getProductStorage());
						dto.setProductImage(orderDetail.getProduct().getProductImageColor());
						dto.setProductColor(orderDetail.getProduct().getProductColor());
						
					}
					
					dto.setUnitPrice(orderDetail.getUnitPrice());
					if (null != orderDetail.getSale()) {
						
						dto.setSales(orderDetail.getSale());
//						if (orderDetail.getSale().getSaleType() == YeeCommonConst
//								.SALE.VOUCHER) {
//							
//							dto.setDiscount(orderDetail.getSale().getDisCount()+"K");
//							dto.setVoucher(YeeStringUtils.join("VOUCHER: ",orderDetail.getSale().getVoucherNm(),"giảm",orderDetail.getSale().getDisCount()));
//						} else {
//							
//							dto.setDiscount(orderDetail.getSale().getDisCount() +"%");
//						}
					}
					dto.setDiscount(orderDetail.getDiscount().toString());
					orderInfoDto.getDetails().add(dto);
				}

				List<String> addressList= new ArrayList<>();
				addressList.add(order.getWardNm());
				addressList.add(order.getDistrictNm());
				addressList.add(order.getProvinceNm());
				
				orderInfoDto.setOrderAddress(YeeStringUtils.yeeJoinStringByCharacter(", ",addressList));
				
				orderInfoDto.setOrderDetailAddress(order.getDetailAddress());
				
				String statusStr = YeeStringUtils.getOrderStatusCode(order.getStatus().toString());
				if (YeeStringUtils.isNotEmpty(statusStr)) {
					
					orderInfoDto.setOrderStatus(statusStr);	
				}
				
				orderInfoDto.setOrderStatusCd(order.getStatus().toString());
				
			    orderInfoDto.setTransactionNo(null == order.getTransactionNo() ? YeeStringUtils.EMPTY : order.getTransactionNo());
			    orderInfoDto.setTransactionStatus(order.getTransStatus());
			    
			    orderInfoDto.setOrderDspt(null == order.getOrderDspt() ? YeeStringUtils.EMPTY : order.getOrderDspt());
			    
			    BigDecimal amountDecimal = new BigDecimal(YeeStringUtils.removeComma(orderInfoDto.getAmount()));
			    BigDecimal feeDecimal = new BigDecimal(YeeStringUtils.removeComma(orderInfoDto.getShipFee()));
			    String notFeePriceStr = YeeDecimalUtils.formatDecimalWithComma(amountDecimal.subtract(feeDecimal));
			    
			    orderInfoDto.setNotFeePrice(notFeePriceStr);
			    
			}
		}
		
	}

	@Override
	public void getAllOrder(List<OrderInfoDto> orderInfoDtos) {
		
		User user = this.userService.validateUser();
		List<Orders> orders = new ArrayList<>();
		if (YeeStringUtils.equals(user.getRole().getRoleName(), "ADMIN")) {
			
			orders = this.orderRepository.findAll();
		} else {
			orders = this.orderRepository.findByUser(user);
		}

		if (0 < orders.size()) {
			orders.stream().forEach(order -> {
				OrderInfoDto orderInfoDto = new OrderInfoDto();
				
				// order info
				orderInfoDto.setOrderId(order.getOrderId());
				orderInfoDto.setAmount(order.getAmount());
				orderInfoDto.setOrderDate(order.getOrderDate());
				orderInfoDto.setOrderUser(order.getOrderUser());
				
				// pay method
				orderInfoDto.setPayMethod(order.getPayMent().getPayNm());
				
				// order receipt info
				orderInfoDto.setDelivery(order.getDelivery().getDeliveryNm());
				
				String statusStr = YeeStringUtils.getOrderStatusCode(order.getStatus().toString());
				if (YeeStringUtils.isNotEmpty(statusStr)) {
					
					orderInfoDto.setOrderStatus(statusStr);	
				}
				
				orderInfoDto.setDetails(new ArrayList<>());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order);
				details.stream().forEach(orderDetail -> {
						
					OrderDetailDto dto = new OrderDetailDto();
					dto.setNewPrice(orderDetail.getNewPrice());
					dto.setOrderQty(orderDetail.getQuantity());
					if (null != orderDetail.getProduct()) {
						
						dto.setProductNm(orderDetail.getProduct().getProductNm());
						dto.setProductImage(orderDetail.getProduct().getProductImage().getMainImage());
					}
					dto.setDiscount(orderDetail.getDiscount().toString());
					if (orderDetail.getSale() != null) {
						
						dto.setSales(orderDetail.getSale());
					}
					orderInfoDto.getDetails().add(dto);
				});
				
				orderInfoDtos.add(orderInfoDto);
			});
		}
	}

	@Override
	public Boolean updateOrderStatus(HttpServletRequest request,
			 
		YeeMessage yeeMessage, Boolean updateFlg) {

		String id = request.getParameter("orderId");
		
		Optional<Orders> orderOptional = this.orderRepository.findById(id);
		
		Orders orders = orderOptional.isPresent() ? orderOptional.get() : null;
		
		if (null == orders) {
			
			yeeMessage.setMessageTitle("Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau");
			return false;
		}
		
		Integer status =  orders.getStatus();
		
		if (status <= 2) {
			
			if (updateFlg) {
				
				orders.setStatus(status + 1);
				if (orders.getStatus() == 3) {
					
					createDeliveryOrder(orders,yeeMessage);
					if (YeeStringUtils.isNotEmpty(yeeMessage.getMessageTitle())) {
						
						return false;
					}
					
				}
			} else {
				
				orders.setStatus(9);
			}
		}
		
		orders = this.orderRepository.save(orders);
		
		if (updateFlg) {
			
			yeeMessage.setMessageTitle("Cập nhật trạng thái đơn hàng thành công");
		} else {

			List<OrderDetails> orderDetails = this.orderDetailsRepository.findByOrder(orders);
			
			Boolean chkBoolean = true;
			
			for(OrderDetails oDetails : orderDetails) {
				
				if (oDetails.getProduct() == null) {
					
					chkBoolean= false;
					break;
				} else {
					
					Product product = oDetails.getProduct();
					
					product.setProductQuantity(product.getProductQuantity()+Integer.valueOf(oDetails.getQuantity()));
					
					product.setSoldQty(product.getSoldQty() - Integer.valueOf(oDetails.getQuantity()));
					
					product = this.productRepository.save(product);
				}
			}
			if (chkBoolean) {
				
				this.sendMailUtils.sendMailOrderCancel(orders);
			}
			
			yeeMessage.setMessageTitle("Huỷ đơn hàng thành công");
		}
		
		return true;
	}
	
	private ServiceData getDeliveryService(Orders orders,DeliveryUnit unit,ShopInfo shopInfo) {
		
		String apiUrl = unit.getDeliveryApi() + "/v2/shipping-order/available-services";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", unit.getToken());

		headers.setContentType(MediaType.APPLICATION_JSON);
		
		ServiceRequestBody serviceRequestBody = new ServiceRequestBody();
		
		serviceRequestBody.setShop_id(Integer.valueOf(unit.getShopId()));
		serviceRequestBody.setFrom_district(Integer.valueOf(shopInfo.getShopAddress().getDistrictId()));
		serviceRequestBody.setTo_district(Integer.valueOf(orders.getDistrictId()));

		Gson gson = new Gson();
		String requestBody = gson.toJson(serviceRequestBody);
		
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> res = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
		
		ServiceData serviceData = new ServiceData();
		if (res.getStatusCode().is2xxSuccessful()) {
			
			// get response data
			String json = res.getBody();
			
			DeliveryService deliveryService = gson.fromJson(json, DeliveryService.class);
			
			serviceData = deliveryService.getData().get(0);
			
		}
		
		return serviceData;
	}
	
	private HashMap<String, Object> setParamRequestCreatDeliveryOrder(Orders orders, DeliveryUnit unit,ShopInfo shopInfo) {
		
		HashMap<String, Object> hashMap = new HashMap<>();
		
		if (orders.getPayMent().getPayId().equals("PAY01")) {
			
			hashMap.put("payment_type_id", 2);
		} else {
			
			hashMap.put("payment_type_id", 1);
		}
		
		hashMap.put("note", orders.getOrderDspt());
		hashMap.put("required_note", "KHONGCHOXEMHANG");
		hashMap.put("return_phone", shopInfo.getShopTel());
		
		String address = YeeStringUtils.join(shopInfo.getShopAddress().getHamlet(),",",shopInfo.getShopAddress().getCommune(),",",shopInfo.getShopAddress().getDistrict(),",",shopInfo.getShopAddress().getProvince());
		String recieptAddress = YeeStringUtils.join(orders.getDetailAddress(),",",orders.getWardNm(),",",orders.getDistrictNm(),",",orders.getProvinceNm());
		hashMap.put("return_address", address);
		hashMap.put("return_address", address);
		hashMap.put("from_name", shopInfo.getShopNm());
		hashMap.put("from_phone", shopInfo.getShopTel());
		hashMap.put("from_address", address);
		hashMap.put("from_ward_name", shopInfo.getShopAddress().getCommune());
		hashMap.put("from_district_name", shopInfo.getShopAddress().getDistrict());
		hashMap.put("from_province_name", shopInfo.getShopAddress().getProvince());
		hashMap.put("to_name", orders.getReceptNm());
		hashMap.put("to_phone", orders.getTel());
		hashMap.put("to_address", recieptAddress);
		hashMap.put("to_ward_code", orders.getWardId().toString());
		hashMap.put("to_district_id", orders.getDistrictId());
		//hashMap.put("to_province_name", orders.getProvinceNm());
		hashMap.put("cod_amount", Integer.parseInt(YeeStringUtils.removeComma(orders.getShipFee())));
		hashMap.put("weight", 200);
		hashMap.put("height", 10);
		hashMap.put("width", 10);
		hashMap.put("length", 10);
		
		ServiceData serviceData = getDeliveryService(orders,unit,shopInfo);
		hashMap.put("service_id", serviceData.getServiceTypeId());
		hashMap.put("service_type_id", serviceData.getServiceTypeId());
		List<HashMap<String,Object>> items = new ArrayList<>();
		
		List<OrderDetails> details = this.orderDetailsRepository.findByOrder(orders);
		details.stream().forEach(e -> {
			
			HashMap<String,Object> map = new HashMap<>();
			if (e.getProduct() !=null) {
				
				map.put("name", e.getProduct().getProductNm());
			}
			map.put("name", "UnKnownProduct");
			map.put("quantity", Integer.parseInt(e.getQuantity()));
			
			items.add(map);
		});
		
		hashMap.put("items", items);
		return hashMap;
	}
	
	private void createDeliveryOrder(Orders orders, YeeMessage yeeMessage) {
		
		String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		DeliveryUnit deliveryUnit = this.deliveryUnitRepository.findById("GHN0001").get();
		
		ShopInfo shopInfo = this.headerRepository.getShopInfo();
		
		headers.add("token", deliveryUnit.getToken());
		
		headers.add("shop_id", deliveryUnit.getShopId());

		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HashMap<String, Object> hashMap= this.setParamRequestCreatDeliveryOrder(orders,deliveryUnit,shopInfo);

		Gson gson = new Gson();
		String requestBody = gson.toJson(hashMap);
		
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		
		try {
			ResponseEntity<String> res = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				
				// get response data
				String json = res.getBody();

				HashMap<String, Object> hashMap2 = gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
				
				Object obj = hashMap2.get("data");
				String json2 = gson.toJson(obj);
			    Map<String, Object> map = gson.fromJson(json2, new TypeToken<Map<String, Object>>() {}.getType());
				
			    
			    String orderCodeString = map.get("order_code").toString();
				orders.setDeliveryCode(orderCodeString);

			}
		} catch (HttpClientErrorException e) {
			
			String json = e.getResponseBodyAsString();
			HashMap<String, Object> hashMap2 = gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
			
			yeeMessage.setMessageTitle("Lên đơn không thành công vì đơn hàng " + orders.getOrderId()+ " có "+ hashMap2.get("code_message_value"));
			return;
		}
		
		
		
	}

	@Override
	public List<OrderInfoDto> filterOrders(HttpServletRequest request) throws ParseException{

		String status = request.getParameter("status");
		
		String dateStart = request.getParameter("dateStart");
		
		String dateEnd = request.getParameter("dateEnd");
		
		List<Orders> orders = new ArrayList<>();
		orders = this.orderRepository.findAll();
		
		if (orders.size() > 0  && YeeStringUtils.isNumbericChk(status) && !YeeStringUtils.equals("-1", status)) {
			
			orders = orders.stream().filter(e-> status.equals(e.getStatus().toString())).collect(Collectors.toList());
		}
		
		 
		if (YeeDateTimeUtils.validateDateMinus(dateStart)) {
			
			
			orders = orders.stream().filter(e-> {
				try {
					return YeeDateTimeUtils.calcDaysBetween2Dates(dateStart, YeeDateTimeUtils.convertYmdhms2YmdMinus(e.getOrderDate())) >= 0;
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				return false;
			} ).collect(Collectors.toList());
		}
		
		if (YeeDateTimeUtils.validateDateMinus(dateEnd)) {
			
			orders = orders.stream().filter(e-> {
				try {
					return YeeDateTimeUtils.calcDaysBetween2Dates(YeeDateTimeUtils.convertYmdhms2YmdMinus(e.getOrderDate()),dateEnd) >= 0;
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return false;
			}).collect(Collectors.toList());
		}
		
		List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
		
		if (0 < orders.size()) {
			orders.stream().forEach(order -> {
				OrderInfoDto orderInfoDto = new OrderInfoDto();
				
				// order info
				orderInfoDto.setOrderId(order.getOrderId());
				orderInfoDto.setAmount(order.getAmount());
				orderInfoDto.setOrderDate(order.getOrderDate());
				orderInfoDto.setOrderUser(order.getOrderUser());
				
				// pay method
				orderInfoDto.setPayMethod(order.getPayMent().getPayNm());
				
				// order receipt info
				orderInfoDto.setDelivery(order.getDelivery().getDeliveryNm());
				
				String statusStr = YeeStringUtils.getOrderStatusCode(order.getStatus().toString());
				if (YeeStringUtils.isNotEmpty(statusStr)) {
					
					orderInfoDto.setOrderStatus(statusStr);	
				}
				
				orderInfoDto.setDetails(new ArrayList<>());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order);
				details.stream().forEach(orderDetail -> {
						
					OrderDetailDto dto = new OrderDetailDto();
					dto.setNewPrice(orderDetail.getNewPrice());
					dto.setOrderQty(orderDetail.getQuantity());
					if(null != orderDetail.getProduct()) {
						
						dto.setProductNm(orderDetail.getProduct().getProductNm());
						dto.setProductImage(orderDetail.getProduct().getProductImage().getMainImage());
					}

					dto.setDiscount(orderDetail.getDiscount().toString());
					if (orderDetail.getSale() != null) {
						
						dto.setSales(orderDetail.getSale());
					}
					orderInfoDto.getDetails().add(dto);
				});
				
				orderInfoDtos.add(orderInfoDto);
			});
		}
		
		return orderInfoDtos;
	}

	@Override
	public void getAllUnDeliveryOrder(List<OrderInfoDto> orderInfoDtos) {
		
		List<Orders> orders = new ArrayList<>();
		orders = this.orderRepository.findByUndeliverOrder();

		if (0 < orders.size()) {
			orders.stream().forEach(order -> {
				
				OrderInfoDto orderInfoDto = new OrderInfoDto();
				
				// order info
				orderInfoDto.setOrderId(order.getOrderId());
				orderInfoDto.setAmount(order.getAmount());
				orderInfoDto.setOrderDate(order.getOrderDate());
				orderInfoDto.setOrderUser(order.getOrderUser());
				
				// pay method
				orderInfoDto.setPayMethod(order.getPayMent().getPayNm());
				
				// order receipt info
				orderInfoDto.setDelivery(order.getDelivery().getDeliveryNm());
				
				String statusStr = YeeStringUtils.getOrderStatusCode(order.getStatus().toString());
				if (YeeStringUtils.isNotEmpty(statusStr)) {
					
					orderInfoDto.setOrderStatus(statusStr);	
				}
				
				orderInfoDto.setDetails(new ArrayList<>());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order);
				details.stream().forEach(orderDetail -> {
						
					OrderDetailDto dto = new OrderDetailDto();
					dto.setNewPrice(orderDetail.getNewPrice());
					dto.setOrderQty(orderDetail.getQuantity());
					
					if (orderDetail.getProduct() != null) {
						
						dto.setProductNm(orderDetail.getProduct().getProductNm());
						dto.setProductImage(orderDetail.getProduct().getProductImage().getMainImage());
					}
					
					dto.setDiscount(orderDetail.getDiscount().toString());
					if (orderDetail.getSale() != null) {
						
						dto.setSales(orderDetail.getSale());
					}
					orderInfoDto.getDetails().add(dto);
				});
				
				orderInfoDtos.add(orderInfoDto);
			});
		}
	}

	@Override
	public Boolean deliveryOrders(HttpServletRequest request, YeeMessage yeeMessage) {
		
			List<Orders> orders = this.orderRepository.findAll();
			if (0 < orders.size()) {
				orders.stream().forEach(order -> {
					
					
					Integer status =  order.getStatus();
					
					if (status <= 2) {
						
						
							
							order.setStatus(3);
							if (order.getStatus() == 3) {
								
								createDeliveryOrder(order,yeeMessage);
								if (YeeStringUtils.isNotEmpty(yeeMessage.getMessageTitle())) {
									
									return;
								}
								
							}
					}
					
					
				});
			}
			
			if (YeeStringUtils.isNotEmpty(yeeMessage.getMessageTitle())) {
				
				return false;
			}
			orders = this.orderRepository.saveAll(orders);

			yeeMessage.setMessageTitle("Đã lên đơn hàng loạt thành công");
			
			return true;
	}

	@Override
	public void getNewestOrder(List<OrderInfoDto> orderInfoDtos) {


		List<Orders> orders = this.orderRepository.findAll();

		if (0 < orders.size()) {
			orders.stream().forEach(order -> {
				OrderInfoDto orderInfoDto = new OrderInfoDto();
				
				// order info
				orderInfoDto.setOrderId(order.getOrderId());
				orderInfoDto.setAmount(order.getAmount());
				orderInfoDto.setOrderDate(order.getOrderDate());
				orderInfoDto.setOrderUser(order.getOrderUser());
				
				// pay method
				orderInfoDto.setPayMethod(order.getPayMent().getPayNm());
				
				// order receipt info
				orderInfoDto.setDelivery(order.getDelivery().getDeliveryNm());
				
				String statusStr = YeeStringUtils.getOrderStatusCode(order.getStatus().toString());
				if (YeeStringUtils.isNotEmpty(statusStr)) {
					
					orderInfoDto.setOrderStatus(statusStr);	
				}
				
				orderInfoDto.setDetails(new ArrayList<>());
				
				List<OrderDetails> details = this.orderDetailsRepository.findByOrder(order);
				details.stream().forEach(orderDetail -> {
						
					OrderDetailDto dto = new OrderDetailDto();
					dto.setNewPrice(orderDetail.getNewPrice());
					dto.setOrderQty(orderDetail.getQuantity());
					
					if (null != orderDetail.getProduct()) {
						
						dto.setProductNm(orderDetail.getProduct().getProductNm());
						dto.setProductImage(orderDetail.getProduct().getProductImage().getMainImage());
					}
					
					dto.setDiscount(orderDetail.getDiscount().toString());
					if (orderDetail.getSale() != null) {
						
						dto.setSales(orderDetail.getSale());
					}
					orderInfoDto.getDetails().add(dto);
				});
				
				orderInfoDtos.add(orderInfoDto);
			});
		}
		
	}
}
