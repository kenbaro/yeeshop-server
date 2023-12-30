package com.yeeshop.yeeserver.domain.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.Gson;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeError;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.constant.YeeMessageConst;
import com.yeeshop.yeeserver.domain.dto.cart.CartItem;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;
import com.yeeshop.yeeserver.domain.dto.checkout.CheckoutDto;
import com.yeeshop.yeeserver.domain.dto.checkout.DeliveryServiceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.DeliveryUnitDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.Cart;
import com.yeeshop.yeeserver.domain.entity.DeliveryUnit;
import com.yeeshop.yeeserver.domain.entity.OrderDetails;
import com.yeeshop.yeeserver.domain.entity.Orders;
import com.yeeshop.yeeserver.domain.entity.PayMent;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.ShopInfo;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.model.deliveryservice.DeliveryService;
import com.yeeshop.yeeserver.domain.model.deliveryservice.ServiceData;
import com.yeeshop.yeeserver.domain.model.deliveryservice.ServiceRequestBody;
import com.yeeshop.yeeserver.domain.model.district.DistrictGHN;
import com.yeeshop.yeeserver.domain.model.district.DistrictGHNData;
import com.yeeshop.yeeserver.domain.model.fee.DeliveryFee;
import com.yeeshop.yeeserver.domain.model.fee.FeeRequestBody;
import com.yeeshop.yeeserver.domain.model.province.ProvinceGHN;
import com.yeeshop.yeeserver.domain.model.ward.WardGHN;
import com.yeeshop.yeeserver.domain.model.ward.WardGHNData;
import com.yeeshop.yeeserver.domain.model.province.ProvinccGHNData;
import com.yeeshop.yeeserver.domain.repository.ICartRepository;
import com.yeeshop.yeeserver.domain.repository.IDeliveryUnitRepository;
import com.yeeshop.yeeserver.domain.repository.IHeaderRepository;
import com.yeeshop.yeeserver.domain.repository.IOrderDetailsRepository;
import com.yeeshop.yeeserver.domain.repository.IOrderRepository;
import com.yeeshop.yeeserver.domain.repository.IPayMentRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.service.ICheckoutService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeSendMailUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CheckoutServiceImpl implements ICheckoutService{
	
	@Autowired
	private IDeliveryUnitRepository deliveryUnitRepository;

	@Autowired 
	private IUserService userService;
	
	@Autowired
	private IHeaderRepository headerRepository;
	
	@Autowired
	private IProductRepository productRepository;
	
	@Autowired 
	private IPayMentRepository payMentRepository;
	
	@Autowired
    private VnPayService vnPayService;
	
	@Autowired 
	private IOrderRepository orderRepository;
	
	@Autowired
	private IOrderDetailsRepository orderDetailsRepository;
	
	@Autowired private ICartRepository cartRepository;
	
	@Autowired private YeeSendMailUtils sendMailUtils;

	@Override
	public void getProvinceInfo(final CheckoutDto checkoutDto) throws JsonParseException, IOException {
		
		String apiUrl = checkoutDto.getDeliveryApi() + "/master-data/province";
		
		checkoutDto.setProvinces(new ArrayList<>());
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", checkoutDto.getDeliveryToken());
		
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		ResponseEntity<String> res = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {

			// get response data
			String json = res.getBody();
			
			Gson gson = new Gson();
			
			ProvinceGHN province = gson.fromJson(json, ProvinceGHN.class);
			
			if (null == checkoutDto.getProvinces()) {
				
				checkoutDto.setProvinces(new ArrayList<>());
			}
			province.getData().stream().forEach(e -> setProvince2Dto(e, checkoutDto));
			
			checkoutDto.setDistrictCd(null);
			if (YeeStringUtils.isEmpty(checkoutDto.getProvinceCd())) {
				
				checkoutDto.setProvinceCd(checkoutDto.getProvinces().get(0).getProvinceID().toString());
			}
		} else {
			
			checkoutDto.setProvinces(new ArrayList<>());
		}
	}

	private void setProvince2Dto(final ProvinccGHNData data,final CheckoutDto checkoutDto) {
		
		AddressProvinceDto provinceDto = new AddressProvinceDto();

		provinceDto.setCountryID(data.getCountryID());
		provinceDto.setProvinceID(data.getProvinceID());
		provinceDto.setProvinceName(data.getProvinceName());
		provinceDto.setCode(data.getCode());
		checkoutDto.getProvinces().add(provinceDto);
	}
   
	private void getDeliveryUnit(final CheckoutDto checkoutDto, String unitId) {
		
		Optional<DeliveryUnit> dUnitOptional = this.deliveryUnitRepository.findById(unitId);
		
		DeliveryUnit deliveryUnit = dUnitOptional.isPresent() ? dUnitOptional.get() : null;
		
		if (null != deliveryUnit) {
			
			checkoutDto.setShopId(deliveryUnit.getShopId());
			checkoutDto.setDeliveryUnitId(deliveryUnit.getDeliveryId());
			checkoutDto.setDeliveryToken(deliveryUnit.getToken());
			checkoutDto.setDeliveryApi(deliveryUnit.getDeliveryApi());
		} else {
			
		}
	}

	@Override
	public void getUserInfo(final CheckoutDto checkoutDto) {
		
		User user = this.userService.validateUser();
		
		checkoutDto.setUserNm(user.getUserNm());
		checkoutDto.setUserEmail(user.getEmail());
		checkoutDto.setUserTel(user.getUserTel());
	}

	private void setDeliveryUnits2List(final DeliveryUnit unit,final CheckoutDto checkoutDto) {
		
		DeliveryUnitDto dto = new DeliveryUnitDto();
		
		dto.setDeliveryUnitId(unit.getDeliveryId());
		dto.setDeliveryUnitNm(unit.getDeliveryNm());
		
		checkoutDto.getDeliveryUnits().add(dto);
	}
	
	private void setDistrict2Dto(final DistrictGHNData data,final CheckoutDto checkoutDto) {
		
		if (!YeeStringUtils.isNumbericChk(data.getDistrictName())) {
			
			AddressDistrictDto districtDto = new AddressDistrictDto();
			
			districtDto.setDistrictID(data.getDistrictID());
			
			districtDto.setDistrictName(data.getDistrictName());
			
			districtDto.setProvinceID(data.getProvinceID());
			
			districtDto.setCode(data.getCode());
			
			districtDto.setType(data.getType());
			
			districtDto.setSupportType(data.getSupportType());
			
			checkoutDto.getDistricts().add(districtDto);
		}
	}
	
	@Override
	public void getDeliveryUnitInfo(final CheckoutDto checkoutDto) {
		
		this.setFromAddress(checkoutDto);

		List<DeliveryUnit> deliveryUnits = this.deliveryUnitRepository.findAll();
		
		if (null != deliveryUnits && 0 < deliveryUnits.size()) {
			
			if (YeeStringUtils.isEmpty(checkoutDto.getDeliveryUnitId())) {
				
				DeliveryUnit unit = deliveryUnits.get(0);
				this.getDeliveryUnit(checkoutDto,unit.getDeliveryId());
				checkoutDto.setDeliveryUnits(new ArrayList<>());
				deliveryUnits.stream().forEach(d -> setDeliveryUnits2List(d, checkoutDto));
			} else {
				
				this.getDeliveryUnit(checkoutDto,checkoutDto.getDeliveryUnitId());
			}

		}
	}

	@Override
	public void getDistrictInfo(CheckoutDto checkoutDto) throws JsonParseException, IOException {

		String apiUrl = checkoutDto.getDeliveryApi() + "/master-data/district";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", checkoutDto.getDeliveryToken());

		headers.set("Content-Type", "application/json");
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
		        .queryParam("province_id", checkoutDto.getProvinceCd());
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		
		ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {
			
			// get response data
			String json = res.getBody();
			
			Gson gson = new Gson();
			
			DistrictGHN district = gson.fromJson(json, DistrictGHN.class);
			
			checkoutDto.setDistricts(new ArrayList<>());
			
			district.getData().stream().forEach(e -> setDistrict2Dto(e, checkoutDto));
			
			if (YeeStringUtils.isEmpty(checkoutDto.getDistrictCd())) {
				
				checkoutDto.setDistrictCd(checkoutDto.getDistricts().get(0).getDistrictID().toString());
			} else {
				
				checkoutDto.setWardCd(null);
			}
			
		} else {
			
			checkoutDto.setDistricts(new ArrayList<>());
		}
	}

	private void setWard2Dto(final WardGHNData data, final CheckoutDto checkoutDto) {
		
		AddressWardDto dto = new AddressWardDto();
		
		dto.setWardId(data.getWardCode());
		dto.setWardNm(data.getWardName());
		dto.setDistrictId(data.getDistrictID());
		
		checkoutDto.getWards().add(dto);
	}

	@Override
	public void getWardInfo(CheckoutDto checkoutDto) throws JsonParseException, IOException {
		
		String apiUrl = checkoutDto.getDeliveryApi() + "/master-data/ward";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", checkoutDto.getDeliveryToken());

		headers.set("Content-Type", "application/json");

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
		        .queryParam("district_id", checkoutDto.getDistrictCd());
		HttpEntity<String> entity = new HttpEntity<String>("body", headers);
		
		ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {
			
			// get response data
			String json = res.getBody();
			
			Gson gson = new Gson();
			
			WardGHN ward = gson.fromJson(json, WardGHN.class);
			
			checkoutDto.setWards(new ArrayList<>());

			ward.getData().stream().forEach(e -> setWard2Dto(e, checkoutDto));
			
			if (YeeStringUtils.isEmpty(checkoutDto.getWardCd())) {
				
				checkoutDto.setWardCd(checkoutDto.getWards().get(0).getWardId());
			}
			
		} else {
			
			checkoutDto.setWards(new ArrayList<>());
		}
	}

	private void calcItem(final CartItem item, CheckoutDto checkoutDto) {
		
		if (YeeStringUtils.isNumbericChk(YeeStringUtils.removeComma(checkoutDto.getTempPriceTotal()))) {
			
			BigDecimal tempTotal = new BigDecimal(YeeStringUtils.removeComma(checkoutDto.getTempPriceTotal()));
			
			if (YeeStringUtils.isNumbericChk(YeeStringUtils.removeComma(item.getTempPrice()))) {
				
				BigDecimal temp = new BigDecimal(YeeStringUtils.removeComma(item.getTempPrice()));
				
				tempTotal = tempTotal.add(temp);
			}

			checkoutDto.setTempPriceTotal(YeeDecimalUtils.formatDecimalWithComma(tempTotal));
			
		} else {
			
			checkoutDto.setTempPriceTotal("0");
		}
		
	}
	@Override
	public void initItem(CheckoutDto checkoutDto) {
		
		this.calcAttribute(checkoutDto);

		checkoutDto.setTempPriceTotal("0");
		
		checkoutDto.getCartItems().stream().forEach(cartItem -> calcItem(cartItem,checkoutDto));
		
		
	}

	private void setParamRequestFeeBody(FeeRequestBody feeRequestBody, CheckoutDto checkoutDto) {
		
		feeRequestBody.setService_id(Integer.valueOf(checkoutDto.getDeliveryServiceId()));
		
		if ( null == checkoutDto.getDeliveryServiceTypeId()) {
			
			checkoutDto.getDeliveryServices().stream().forEach(e -> {
					if(YeeStringUtils.equals(e.getServiceId(), checkoutDto.getDeliveryServiceId())) { 
						checkoutDto.setDeliveryServiceTypeId(e.getSeviceTypeId());
					}
				}
			);
		}
			
		feeRequestBody.setService_type_id(Integer.valueOf(checkoutDto.getDeliveryServiceTypeId()));
		
		
		String insurancePriceStr = YeeStringUtils.removeComma(checkoutDto.getTempPriceTotal());
		Integer insurancePrice = Integer.valueOf(insurancePriceStr); 
		feeRequestBody.setInsurance_value(insurancePrice);
		
		feeRequestBody.setCoupon(null);
		
		feeRequestBody.setFrom_district_id(Integer.valueOf(checkoutDto.getFromDistrictId()));
		feeRequestBody.setTo_district_id(Integer.valueOf(checkoutDto.getDistrictCd()));
		feeRequestBody.setTo_ward_code(checkoutDto.getWardCd());
		
		feeRequestBody.setHeight(checkoutDto.getHeightTotal());
		feeRequestBody.setLength(checkoutDto.getLengthTotal());
		feeRequestBody.setWeight(checkoutDto.getWeightTotal());
		feeRequestBody.setWidth(checkoutDto.getWidthTotal());
		
	}
	@Override
	public void calcFeeDelivery(CheckoutDto checkoutDto) {
		
		ShopInfo shopInfo = this.headerRepository.getShopInfo();
		
		if (checkoutDto.getProvinceCd().equals(shopInfo.getShopAddress().getProvinceId())) {
			
			checkoutDto.setDeliveryFee(YeeDecimalUtils.formatDecimalWithComma(new BigDecimal("100000")));
		} else {
			
			String apiUrl = checkoutDto.getDeliveryApi() + "/v2/shipping-order/fee";

			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.add("token", checkoutDto.getDeliveryToken());
			
			headers.add("shop_id", checkoutDto.getShopId());

			headers.setContentType(MediaType.APPLICATION_JSON);
			
			FeeRequestBody feeRequestBody = new FeeRequestBody();
			
			this.setParamRequestFeeBody(feeRequestBody, checkoutDto);

			Gson gson = new Gson();
			String requestBody = gson.toJson(feeRequestBody);
			
			HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
			
			ResponseEntity<String> res = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
			
			if (res.getStatusCode().is2xxSuccessful()) {
				
				// get response data
				String json = res.getBody();

				
				DeliveryFee fee = gson.fromJson(json, DeliveryFee.class);
				
				checkoutDto.setDeliveryFee(YeeDecimalUtils.formatDecimalWithComma(new BigDecimal(fee.getData().getTotal())));

			} else {
				
				checkoutDto.setDistricts(new ArrayList<>());
			}
		}
	}
	
	private void setFromAddress(final CheckoutDto checkoutDto) {
		
		ShopInfo shopInfo = this.headerRepository.getShopInfo();
		
		checkoutDto.setFromDistrictId(shopInfo.getShopAddress().getDistrictId());
	}
	
	private void setDeliverSerive2Dto(ServiceData item, CheckoutDto checkoutDto) {
		
		DeliveryServiceDto dto = new DeliveryServiceDto();
		
		dto.setServiceId(item.getServiceId().toString());
		dto.setServiceNm(item.getShortName());
		dto.setSeviceTypeId(item.getServiceTypeId().toString());
		
		checkoutDto.getDeliveryServices().add(dto);
	}

	@Override
	public void getDeliveryService(CheckoutDto checkoutDto) {
		
		String apiUrl = checkoutDto.getDeliveryApi() + "/v2/shipping-order/available-services";

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("token", checkoutDto.getDeliveryToken());

		headers.setContentType(MediaType.APPLICATION_JSON);
		
		ServiceRequestBody serviceRequestBody = new ServiceRequestBody();
		
		serviceRequestBody.setShop_id(Integer.valueOf(checkoutDto.getShopId()));
		serviceRequestBody.setFrom_district(Integer.valueOf(checkoutDto.getFromDistrictId()));
		serviceRequestBody.setTo_district(Integer.valueOf(checkoutDto.getDistrictCd()));

		Gson gson = new Gson();
		String requestBody = gson.toJson(serviceRequestBody);
		
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> res = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
		
		if (res.getStatusCode().is2xxSuccessful()) {
			
			// get response data
			String json = res.getBody();
			
			DeliveryService deliveryService = gson.fromJson(json, DeliveryService.class);
			
			checkoutDto.setDeliveryServices(new ArrayList<>());
			
			deliveryService.getData().stream().forEach(item -> setDeliverSerive2Dto(item,checkoutDto));
			
			if (YeeStringUtils.isEmpty(checkoutDto.getDeliveryServiceId())) {
				
				checkoutDto.setDeliveryServiceId(checkoutDto.getDeliveryServices().get(0).getServiceId());
			}
			if (YeeStringUtils.isEmpty(checkoutDto.getDeliveryServiceTypeId())) {
				
				checkoutDto.setDeliveryServiceTypeId(checkoutDto.getDeliveryServices().get(0).getSeviceTypeId());
			}
			
		} else {
			
			checkoutDto.setDeliveryServices(new ArrayList<>());
		}
	}
	
	private void setAttribute2Dto(CartItem item, CheckoutDto checkoutDto) {
		
		
		
		Optional<Product> productOptional = this.productRepository.findById(item.getSKU());
		Product product = productOptional.isPresent() ? productOptional.get() : null;
		
		if (null != product) {
			
			checkoutDto.setWeightTotal(checkoutDto.getWeightTotal() + (int)Math.round(Double.valueOf(product.getProductAttribute().getWeight())));
			checkoutDto.setLengthTotal(checkoutDto.getLengthTotal() + (int)Math.round(Double.valueOf(product.getProductAttribute().getLength())));
			checkoutDto.setWidthTotal(checkoutDto.getWidthTotal() + (int)Math.round(Double.valueOf(product.getProductAttribute().getWidth())));
			checkoutDto.setHeightTotal(checkoutDto.getHeightTotal() + (int)Math.round(Double.valueOf(product.getProductAttribute().getHeight())));
			
		}

	}
	private void calcAttribute(CheckoutDto checkoutDto) {
		
		checkoutDto.setHeightTotal(0);
		checkoutDto.setWeightTotal(0);
		checkoutDto.setLengthTotal(0);
		checkoutDto.setWidthTotal(0);
		
		checkoutDto.getCartItems().forEach(item -> setAttribute2Dto(item, checkoutDto));
		
	}

	@Override
	public void calcTotalPrice(CheckoutDto checkoutDto) {
		
		BigDecimal tempPriceTotal = new BigDecimal(YeeStringUtils.removeComma(checkoutDto.getTempPriceTotal()));
		BigDecimal fee = new BigDecimal(YeeStringUtils.removeComma(checkoutDto.getDeliveryFee()));
		
		BigDecimal totalPrice = tempPriceTotal.add(fee);
		checkoutDto.setTotalPrice(YeeDecimalUtils.formatDecimalWithComma(totalPrice));
	}

	@Override
	public void getPaymentMethod(CheckoutDto checkoutDto) {
		
		List<PayMent> payMents = this.payMentRepository.findAll();
		
		if (0 < payMents.size()) {
			
			checkoutDto.setPayMents(payMents);
		}
		
		if (YeeStringUtils.isEmpty(null)) {
			
			checkoutDto.setPayId(payMents.get(0).getPayId());
		}
		
	}

	@Override
	public String createPayment(CheckoutDto checkoutDto, YeeMessageDto yeeMessageDto) {
		
		String payId =  checkoutDto.getPayId();
		
		String vnpayUrl = YeeStringUtils.EMPTY;

		Boolean isError = false;
		
		Orders order = new Orders();
		
		User user = this.userService.validateUser();
		
		String currentTime = YeeDateTimeUtils.getCurrentDateTime();
		String orderId = YeeStringUtils.join(YeeCommonConst.CART.ORDER_CD, YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(currentTime));

		List<OrderDetails> details = new ArrayList<>();
		
		if (YeeStringUtils.isNoneEmpty(payId)) {
			
			Optional<PayMent> payOptional = this.payMentRepository.findById(payId);
			
			PayMent payMent = payOptional.isPresent() ? payOptional.get() :null;
			if (null != payMent) {
				
				this.getDeliveryUnit(checkoutDto, checkoutDto.getDeliveryUnitId());
				this.setFromAddress(checkoutDto);
				this.initItem(checkoutDto);
				this.calcFeeDelivery(checkoutDto);
				this.calcTotalPrice(checkoutDto);
				
				String totalPrice = YeeStringUtils.removeComma(checkoutDto.getTotalPrice());
				if (YeeStringUtils.isNotEmpty(totalPrice) && YeeStringUtils.isNumbericChk(totalPrice) && !isError) {
					
					
					if (YeeStringUtils.equals(payId, YeeCommonConst.PAYMENT.VNPAY_CD)) {
						
						Integer price = Integer.valueOf(totalPrice);
						String baseUrl = "http://localhost:3001/checkout/"+orderId;
						
						String title = YeeStringUtils.join(user.getUserNm() + YeeMessageConst.PAY_TITLE_0001);
						vnpayUrl = this.vnPayService.createOrder(price, title, baseUrl);
					} else {
						
						vnpayUrl = "/order-return/"+orderId;
					}
					
					
					order.setOrderId(orderId);
					order.setOrderDate(currentTime);
					order.setReceptNm(checkoutDto.getFullNm());
					order.setTel(checkoutDto.getPhone());
					order.setAmount(checkoutDto.getTotalPrice());
					order.setShipFee(checkoutDto.getDeliveryFee());
					order.setOrderDspt(checkoutDto.getNote());
					order.setPayMent(payMent);
					
					order.setOrderUser(checkoutDto.getUserNm());
					order.setUserMail(checkoutDto.getUserEmail());
					Optional<DeliveryUnit> unitOptional = this.deliveryUnitRepository.findById(checkoutDto.getDeliveryUnitId());
					DeliveryUnit unit = unitOptional.isPresent() ? unitOptional.get() : null;
					
					if (null == unit) {

						isError = true;
					} else {
						
						order.setDelivery(unit);
					} 
					
					order.setUser(user);
					
					order.setProvinceId(Integer.valueOf(checkoutDto.getProvinceCd()));
					order.setDistrictId(Integer.valueOf(checkoutDto.getDistrictCd()));
					order.setWardId(Integer.valueOf(checkoutDto.getWardCd()));
					order.setDetailAddress(checkoutDto.getDetailAddress());
					for(AddressProvinceDto p: checkoutDto.getProvinces()) {
						
						if (YeeStringUtils.equals(p.getProvinceID().toString(), checkoutDto.getProvinceCd())) {
							
							order.setProvinceId(p.getProvinceID());		
							order.setProvinceNm(p.getProvinceName());							
						}
					}
					
					for (AddressDistrictDto d : checkoutDto.getDistricts()) {
						
						if (YeeStringUtils.equals(d.getDistrictID().toString(), checkoutDto.getDistrictCd())) {
													
							order.setDistrictId(d.getDistrictID());		
							order.setDistrictNm(d.getDistrictName());;							
						}
					}
					
					for (AddressWardDto w: checkoutDto.getWards()) {
						
						if (YeeStringUtils.equals(w.getWardId().toString(), checkoutDto.getWardCd())) {
							
							order.setWardId(Integer.valueOf(w.getWardId()));		
							order.setWardNm(w.getWardNm());							
						}
					}
					order.setStatus(0);
					order.setTransStatus(String.valueOf(0));				
					if (0 < checkoutDto.getCartItems().size()) {
						
						String oDtlId = YeeStringUtils
								.join(YeeCommonConst.CART.ORDER_DETAIL_CD, YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(currentTime));
						
						
						for (int idx = 0; idx < checkoutDto.getCartItems().size(); idx++) {
							
							OrderDetails orderDetail = new OrderDetails();
							
							orderDetail.setODetailsId(YeeStringUtils.join(oDtlId,String.valueOf(idx)));
							orderDetail.setOrder(order);
							
							Product product = this.productRepository.findActiveProductBySKU(checkoutDto.getCartItems().get(idx).getSKU());
							orderDetail.setProduct(product);
							
							orderDetail.setNewPrice(checkoutDto.getCartItems().get(idx).getNewPrice());
							orderDetail.setUnitPrice(checkoutDto.getCartItems().get(idx).getUnitPrice());
							orderDetail.setDiscount(product.getSale().getDisCount());
							orderDetail.setSale(product.getSale());;
							if (null != checkoutDto.getCartItems().get(idx).getSales()) {
								
								orderDetail.setSale(checkoutDto.getCartItems().get(idx).getSales());
							}
							
							orderDetail.setQuantity(checkoutDto.getCartItems().get(idx).getOrderQty());
							
							details.add(orderDetail);						
						}
					} else {
						
						isError = true;
					}
				}
			}
		}
		
		if (isError) {
			
			// set Error Flag
			yeeMessageDto.setIsError(isError);

			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Đã xảy ra lỗi trông quá trình thanh toán, vui lòng thử lại !");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
		} else {
			
			Orders orderSave = this.orderRepository.save(order);
			
			if (null != orderSave) {
				
				for(int i = 0; i < details.size(); i++) {
					
					OrderDetails orderDetailsSave = this.orderDetailsRepository.save(details.get(i));
					
								
					if (null != orderDetailsSave) {
						
						Product product = orderDetailsSave.getProduct();
						
						product.setProductQuantity(Integer.valueOf(product.getProductQuantity()) - Integer.valueOf(orderDetailsSave.getQuantity()));
						
						product.setSoldQty(Integer.valueOf(product.getSoldQty() + Integer.valueOf(orderDetailsSave.getQuantity())));
						
						product = this.productRepository.save(product);
						
						Optional<Cart> cartOptional = this.cartRepository.findCartofCustomerById(user.getUserCd(), checkoutDto.getCartItems().get(i).getCartId());
						
						if (cartOptional.isPresent()) {
							
							this.cartRepository.delete(cartOptional.get());
						}
						
						if (YeeStringUtils.startsWith(vnpayUrl, "/order-return/")) {
							
							this.sendMailUtils.sendMailOrderSuccess(order);
						}
					}
				}
			} else {
				isError = true;
			}
			
			if (isError) {
				
				// set Error Flag
				yeeMessageDto.setIsError(isError);

				YeeMessage msg = new YeeMessage();

				// set Message :
				msg.setMessageTitle("Đã xảy ra lỗi trông quá trình thanh toán, vui lòng thử lại !");;
				msg.setMessageType(YeeError.ERROR);
				yeeMessageDto.getMessages().add(msg);
				
			}
		}
		
		
		return vnpayUrl;
		
	}

	@Override
	public void updatePaymentCallBack(String orderId, HttpServletRequest request, Boolean isSuccess) {
	     
		Optional<Orders> orderOptional = this.orderRepository.findById(orderId);
		
		Orders order = orderOptional.isPresent() ? orderOptional.get() : null;
		
		if (null != order) {

			List<OrderDetails> orderDetails = order.getOrderDetails();
			if (isSuccess) {
				
				//String orderInfo = request.getParameter("vnp_OrderInfo");
				// String paymentTime = request.getParameter("vnp_PayDate");
		        String transactionId = request.getParameter("vnp_TransactionNo");
		        //String totalPrice = request.getParameter("vnp_Amount");
		        
		        order.setTransactionNo(transactionId);
		        order.setTransStatus(YeeCommonConst.ORDER_STATUS.PAY_SUCCESS);	
		        order =  this.orderRepository.save(order);
		        
			} else {
				
				orderDetails.stream().forEach(e -> this.orderDetailsRepository.delete(e));
				this.orderRepository.delete(order);	
				
			} 
				
	    }
	}
}
