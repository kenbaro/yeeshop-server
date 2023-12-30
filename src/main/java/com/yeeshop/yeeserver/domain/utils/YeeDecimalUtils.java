package com.yeeshop.yeeserver.domain.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;


import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_100;
import lombok.experimental.UtilityClass;

@UtilityClass
public class YeeDecimalUtils {

	public BigDecimal calcPriceDiscount(Integer discount, String unitPrice) {
		
		BigDecimal beforePrice = new BigDecimal(unitPrice.replaceAll(",",""));
		
		BigDecimal priceDiscount = new BigDecimal(discount);
		
		//BigDecimal afterPrice = (new BigDecimal(INT_100).subtract(priceDiscount)).divide(new BigDecimal(INT_100), 0, RoundingMode.HALF_EVEN);
		BigDecimal afterPrice = new BigDecimal(INT_100).subtract(priceDiscount).multiply(beforePrice).divide(new BigDecimal(INT_100),0,RoundingMode.HALF_EVEN);
		
		return null != afterPrice ? afterPrice : beforePrice;
	}
	
	public String formatDecimalWithComma(final BigDecimal formatDecimal) {
		
		return String.format("%,.0f", formatDecimal);
	}
	
	public String yeeMultiply(String str1, String str2) {
		
		str1 = YeeStringUtils.removeComma(str1);
		str2 = YeeStringUtils.removeComma(str2);
		
		BigDecimal num1 = new BigDecimal(YeeStringUtils.isNotEmpty(str1) ? str1: "0");
		
		BigDecimal num2 = new BigDecimal(YeeStringUtils.isNotEmpty(str2) ? str2: "0");
		
		return formatDecimalWithComma(num1.multiply(num2));
	}
	
}
