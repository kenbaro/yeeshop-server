package com.yeeshop.yeeserver.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_0;
import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_1;
import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.SALE.SALE_OFF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.CART.CART_CD;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeError;
import com.yeeshop.yeeserver.domain.constant.YeeMessageConst;
import com.yeeshop.yeeserver.domain.dto.cart.CartDto;
import com.yeeshop.yeeserver.domain.dto.cart.CartItem;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.entity.Cart;
import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.ICartRepository;
import com.yeeshop.yeeserver.domain.repository.IProductRepository;
import com.yeeshop.yeeserver.domain.service.ICartService;
import com.yeeshop.yeeserver.domain.service.ICommonService;
import com.yeeshop.yeeserver.domain.service.IUserService;
import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeDecimalUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

@Service
public class CartServiceImpl implements ICartService{

	@Autowired
	ICartRepository cartRepository;
	
	@Autowired
	IProductRepository productRepository;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICommonService commonService;
	
	@Override
	public void initCart(final CartDto cartDto) {
		
		User user = this.userService.validateUser();
		List<CartItem> cartItems = new ArrayList<>();
		
		List<Cart> carts = this.cartRepository.findCartByUserCd(user.getUserCd());
		
		BigDecimal tempPriceTotal = BigDecimal.ZERO;
		
		for(Cart cart : carts) {
			
			CartItem cartItem = new CartItem();
			
			cartItem.setCartId(cart.getCartId());

			Product product = cart.getCartProduct();
			
			cartItem.setSKU(product.getSKU());
			
			cartItem.setUnitPrice(product.getUnitPrice());
			
			cartItem.setOrderQty(cart.getCartQuantity().toString());
			
			cartItem.setProductColor(product.getProductColor());
			
			cartItem.setProductNm(product.getProductNm());
			
			cartItem.setImage(product.getProductImage().getMainImage());
			
			cartItem.setProductQty(product.getProductQuantity());
			
			if (Integer.valueOf(cartItem.getOrderQty()) > Integer.valueOf(cartItem.getProductQty()) 
					|| Integer.valueOf(cartItem.getOrderQty()) == 0) {
				
				cartItem.setDiscount(YeeStringUtils.EMPTY);
				cartItem.setNewPrice(YeeStringUtils.EMPTY);
				cartItem.setTempPrice(YeeStringUtils.EMPTY);
			} else if(product.getSale() != null) {
				
				if (this.commonService.checkSaleExpired(product.getSale())) {

					cartItem.setSales(product.getSale());

					if (product.getSale().getSaleType() == SALE_OFF) {
						
						cartItem.setDiscount(product.getSale().getDisCount().toString());
						
						BigDecimal newPriceDecimal = YeeDecimalUtils
								.calcPriceDiscount(product.getSale().getDisCount(), product.getUnitPrice());
						cartItem.setNewPrice(YeeDecimalUtils.formatDecimalWithComma(newPriceDecimal));
						
						BigDecimal tempPriceBigDecimal = newPriceDecimal.multiply(new BigDecimal(YeeStringUtils.removeComma(cartItem.getOrderQty())));
						
						cartItem.setTempPrice(YeeDecimalUtils.formatDecimalWithComma(tempPriceBigDecimal));
				
						//tempPriceTotal = tempPriceTotal.add(tempPriceBigDecimal);
					} else {
						
						cartItem.setDiscount(INT_0.toString());
						
						
						BigDecimal newPriceDecimal = YeeDecimalUtils
								.calcPriceDiscount(product.getSale().getDisCount(), product.getUnitPrice());
						cartItem.setNewPrice(YeeDecimalUtils.formatDecimalWithComma(newPriceDecimal));
						
						BigDecimal tempPriceBigDecimal = newPriceDecimal.multiply(new BigDecimal(cartItem.getOrderQty()));
						cartItem.setTempPrice(YeeDecimalUtils.formatDecimalWithComma(tempPriceBigDecimal));
						
						//tempPriceTotal = tempPriceTotal.add(tempPriceBigDecimal);
					}
					
				}
				
			} else {
				
				cartItem.setDiscount(INT_0.toString());

				BigDecimal newPriceDecimal = new BigDecimal(YeeStringUtils.removeComma(cartItem.getUnitPrice()));
				BigDecimal tempPriceBigDecimal = newPriceDecimal.multiply(new BigDecimal(cartItem.getOrderQty()));
				cartItem.setTempPrice(YeeDecimalUtils.formatDecimalWithComma(tempPriceBigDecimal));
				
			}
			
			String dateString = YeeDateTimeUtils.getCurrentDate();
			if (product.getIsAvailabled() == INT_1 && product.getProductSeries() != null && YeeDateTimeUtils.calcDaysBetween2Dates(product.getDateStart(),dateString) >=0)	{
				
				cartItem.setIsAvailable(true);
			} else {
				
				cartItem.setIsAvailable(false);
			}
				
			cartItems.add(cartItem);
		}
		
		cartDto.setCartItems(cartItems);
		cartDto.setTempPriceTotal(YeeDecimalUtils.formatDecimalWithComma(tempPriceTotal));
		
		carts = this.cartRepository.findCartByUserCd(user.getUserCd());
		Integer cartCnt = 0;
		for (Cart c: carts) {
			cartCnt += c.getCartQuantity();
		}
		
		cartDto.setCartCnt(cartCnt);
	}

	@Override
	public YeeMessageDto insertCart(final CartDto cartDto, final YeeMessageDto yeeMessageDto) {
		
		
		if (null == yeeMessageDto.getMessages()) {
			
			yeeMessageDto.setMessages(new ArrayList<>());
		}
		
		Boolean serverErr = false;
		if (INT_0 < cartDto.getCartItems().size()) {
			
			serverErr = true;
		}
		
		CartItem cartItem = cartDto.getCartItems().get(INT_0);
		
		if (!this.validateCartItem(cartItem)) {
			
			serverErr = true;
		}
		
		User user = this.userService.validateUser();

		Cart cart = new Cart();

		
		String sku = cartItem.getSKU();
		
		Product product = this.productRepository.findActiveProductBySKU(sku);
		
		if (null == product || product.getProductQuantity() == INT_0) {
			
			// set Error Flag
			yeeMessageDto.setIsError(true);

			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Sản phẩm không khả dụng hoặc tạm thời hết hàng !!!");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			return yeeMessageDto;
			
		} else {
			
			cart = this.cartRepository.findCartByProductId(sku, user.getUserCd());
			
			if (null == cart) {
				
				cart = new Cart();
				String orderIdStr = YeeDateTimeUtils.getCurrentDateTime();
				cart.setCartId(YeeStringUtils.join(CART_CD, YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(orderIdStr)));
				cart.setCartProduct(product);
				cart.setCartQuantity(Integer.valueOf(cartItem.getOrderQty()));
				cart.setCartUser(user);
			} else {
				
				cart.setCartQuantity(cart.getCartQuantity()+Integer.valueOf(cartItem.getOrderQty()));
				
			}
			
			cart = this.cartRepository.save(cart);
			if (null == cart) {
				
				serverErr = true;
			} else {
				
				// set Error Flag
				yeeMessageDto.setIsError(false);

				YeeMessage msg = new YeeMessage();

				// set Message :
				msg.setMessageTitle("Sản phẩm đã được thêm vào giỏ hàng");;
				msg.setMessageType(YeeError.SUCCESS);
				yeeMessageDto.getMessages().add(msg);
				
				return yeeMessageDto;
			}
		}
		
		if (serverErr) {
			
			// set Error Flag
			yeeMessageDto.setIsError(true);

			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Đã xảy ra lỗi từ phía máy chủ, không thể thêm sản phẩm vào giỏ hàng!");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
		}
		
		return yeeMessageDto;
	}
	
	@Override
	public Cart updateCart(final CartDto cartDto, final YeeMessageDto yeeMessageDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Boolean validateCartItem(final CartItem cartItem) {
		
		if (!YeeStringUtils.isNumbericChk(cartItem.getOrderQty()) || Integer.valueOf(YeeStringUtils.removeComma(cartItem.getOrderQty())) < 0) {
			
			return false;
		}
		
//		if (!YeeStringUtils.isNumbericChk(cartDto.getPriceVoucher()) || Integer.valueOf(cartDto.getPriceVoucher()) < 0) {
//			
//			return false;
//		}
		
		if (!YeeStringUtils.isNumbericChk(cartItem.getUnitPrice()) || Integer.valueOf(YeeStringUtils.removeComma(cartItem.getUnitPrice())) < 0) {
			
			return false;
		}
		
//		if (!YeeStringUtils.isNumbericChk(cartDto.getNewPrice()) || Integer.valueOf(cartDto.getNewPrice()) < 0) {
//			
//			return false;
//		}
		return true;
	}

	@Override
	public YeeMessageDto deleteCart(final String cartId, final YeeMessageDto yeeMessageDto) {
		
		if ( null ==  yeeMessageDto.getMessages()) {
			
			yeeMessageDto.setMessages(new ArrayList<>());
		}
		
		User user = this.userService.validateUser();
		
		Optional<Cart> optionalCart = this.cartRepository.findCartofCustomerById(user.getUserCd(),cartId);
		Cart cart = optionalCart.isPresent() ? optionalCart.get() : null;
		
		if (null == cart) {
			
			// set Error Flag
			yeeMessageDto.setIsError(true);
			
			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Sản phẩm không tồn tại trong giỏ hàng, không thể xoá sản phẩm !");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
		} else {
			
			this.cartRepository.delete(cart);
			
			// set Error Flag
			yeeMessageDto.setIsError(false);
			
			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Xoá sản phẩm khỏi giỏ hàng thành công !!!");
			msg.setMessageType(YeeError.SUCCESS);
			yeeMessageDto.getMessages().add(msg);
		}
		return yeeMessageDto;
		
	}

	@Override
	public YeeMessageDto updateCartItem(String cartId, String qty, YeeMessageDto yeeMessageDto) {

		if (!YeeStringUtils.isNumbericChk(qty)) {
			
			// set Error Flag
			yeeMessageDto.setIsError(true);
			
			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Số lượng update không hợp lệ! Không thể cập nhật số lượng giỏ hàng");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
		} else {
			
			User user = this.userService.validateUser();
			
			Optional<Cart> optionalCart = this.cartRepository.findCartofCustomerById(user.getUserCd(),cartId);

			Cart cart = optionalCart.isPresent() ? optionalCart.get() : null;
			
			if (null == cart) {
				
				// set Error Flag
				yeeMessageDto.setIsError(true);
				
				YeeMessage msg = new YeeMessage();

				// set Message :
				msg.setMessageTitle("Sản phẩm không tồn tại trong giỏ hàng, không thể cập nhật!");;
				msg.setMessageType(YeeError.ERROR);
				yeeMessageDto.getMessages().add(msg);
			} else {
				
				if (cart.getCartProduct().getProductQuantity() == 0) {
					
					yeeMessageDto = this.commonService
							.getYeeMessage(YeeMessageConst.YEE003, YeeError.ERROR, yeeMessageDto);
				} else if (Integer.valueOf(qty) > cart.getCartProduct().getProductQuantity()) {
					
					yeeMessageDto = this.commonService
							.getYeeMessage(YeeMessageConst.YEE008, YeeError.ERROR, yeeMessageDto);
				} else {
					
					cart.setCartQuantity(Integer.valueOf(qty));
					
					cart = this.cartRepository.save(cart);
					
					if (null == cart) {
						
						// set Message
						yeeMessageDto = this.commonService
								.getYeeMessage(YeeMessageConst.YEE003, YeeError.ERROR, yeeMessageDto);
					} else {
						
						yeeMessageDto = this.commonService
								.getYeeMessage(YeeMessageConst.YEE001, YeeError.SUCCESS, yeeMessageDto);
					}
				}
			}
			
		}
		
		return yeeMessageDto;
	}

	private Boolean isErrItem(CartItem cartItem, User user) {
		
		if (!YeeStringUtils.isNumbericChk(cartItem.getOrderQty())) {
			
			return true;
		}

		Optional<Cart> optionalCart = this.cartRepository.findCartofCustomerById(user.getUserCd(),cartItem.getCartId());

		Cart cart = optionalCart.isPresent() ? optionalCart.get() : null;
		
		if (null == cart) {
			
			return true;
		} else {
			
			if (cart.getCartProduct().getProductQuantity() == 0 || cart.getCartProduct().getIsAvailabled() == 0) {
				
				return true;
			} else if (Integer.valueOf(cartItem.getOrderQty()) > cart.getCartProduct().getProductQuantity()) {
				
				return true;
			}
		}
		
		return false;
	}
	@Override
	public YeeMessageDto validateBeforeSwitchToPay(final List<CartItem> cartItems, YeeMessageDto yeeMessageDto) {
		
		if ( null ==  yeeMessageDto.getMessages()) {
			
			yeeMessageDto.setMessages(new ArrayList<>());
		}
		
		User user = this.userService.validateUser();
		
		if (null == cartItems) {
			
			yeeMessageDto = this.commonService
					.getYeeMessage(YeeMessageConst.YEE009, YeeError.ERROR, yeeMessageDto);
		}
		
		Boolean chkErrCart = true;
		if (null != cartItems) {
			
			chkErrCart = cartItems.stream().anyMatch(item -> isErrItem(item, user));
		}
			
		
		if (chkErrCart) {
			
			yeeMessageDto = this.commonService
					.getYeeMessage(YeeMessageConst.YEE009, YeeError.ERROR, yeeMessageDto);
		}

		return yeeMessageDto;
	}
	
}
