package com.yeeshop.yeeserver.domain.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import static com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber.INT_0;

import lombok.experimental.UtilityClass;

@UtilityClass
public class YeeStringUtils  extends StringUtils{

	public Boolean chkSpecialCharacter(String s) {
	     if (s == null || s.trim().isEmpty()) {

	         return false;
	     }

	     Pattern p = Pattern.compile("[^A-Za-z0-9]");
	     Matcher m = p.matcher(s);

	     boolean chk = m.find();
	     if (chk) {
	    	 
	    	 return true;
	     }
	        
	     return false;
	 }
	
	public Boolean chkValidEmail(String str) {
		
		if (null == str || str.trim().isEmpty()) {
			
			return false;
		}
		
		Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
	    Matcher m = p.matcher(str);

	    boolean chk = m.find();
	    if (chk) {
	    	 
	    	return true;
	    }
	        
	    return false;
		
	}
	
	public String yeeJoinStringByCharacter(String separator, List<String> strList) {
		
		if (YeeStringUtils.isEmpty(separator)) {
			
			return EMPTY;
		}
		
		if (null == strList || INT_0 == strList.size()) {
			
			return EMPTY;
		}
		
		String afterJoinStr = strList.stream().collect(Collectors.joining(separator));
		
		return afterJoinStr;
	}
	
	public String removeComma(String str) {
		
		if (str == null || str.trim().isEmpty()) {

	         return EMPTY;
	    }
		
		return str.trim().replaceAll(",", "");
	}
	
	public List<String> removeDuplicateElementsStr(List<String> list) {
		
		LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);
		
		return (List<String>) hashSet.stream().collect(Collectors.toList());
	}
	
	public Boolean isNumbericChk (String str) {
		
		return str != null && str.replaceAll(",","").matches("[0-9.]+");
	}
	
	public List<String> getHeightWeightLengthFromStr(String str) {
		
		StringBuilder stringBuilder = new StringBuilder(str.trim());
		
		Integer lastSpace = stringBuilder.lastIndexOf(" ");
		
		if (lastSpace != -1) {
			
			stringBuilder.delete(lastSpace, stringBuilder.length());
		}

		String[] arr = stringBuilder.toString().split("\\s+");
		
		List<String> list = java.util.Arrays.asList(arr);
		
		return list;
	}
	
	
	
public String getPayMentStatusCode(String str) {
		
		if (str == null || str.trim().isEmpty()) {

	         return EMPTY;
	    }
		
		final String PAY_SUCCESS = "1: Đã thanh toán trước";
		
		final String PAY_FAIL = "0: Chưa thanh toán";
		
		List<String> list = new ArrayList<>();
		list.add(PAY_SUCCESS);
		list.add(PAY_FAIL);
		String rtnStr = EMPTY;
		for(String s : list) {
			
			List<String> items = Arrays.asList(s.split(":"));
			if (YeeStringUtils.equals(items.get(0), str)) {

				rtnStr = items.get(1);
			}
		}
		
		return rtnStr;
	}
	public String getOrderStatusCode(String str) {
		
		if (str == null || str.trim().isEmpty()) {

	         return EMPTY;
	    }
		
		/** WAIT_CONFIRM */
		final String WAIT_CONFIRM = "0:Đang chờ xác nhận";
		
		/** PREPARING */
		final String PREPARING = "1:Đơn hàng đang được chuẩn bị";
		
		/** SENT_TO_DELIVERY */
		final String SENT_TO_DELIVERY = "2:Đơn hàng đã sẵn sàng bàn giao cho đơn vị vận chuyển";
		
		/** PREPARE_TO_DELIVERY */
		final String PREPARE_TO_DELIVERY = "3:Đang chờ nhân viên vận chuyển tới lấy hàng";
		
		/** DELIVERING */
		final String DELIVERING = "4:Đang giao hàng";
		
		/** AT_STATION */
		final String AT_STATION = "5:Đơn hàng của bạn đã tới kho";
		
		/** AT_STATION */
		final String DELIVERY_TO_CUS = "6:Đơn hàng đang trên đường giao đến bạn";
		
		final String DELIVERY_SUCCESS = "7:Giao hàng thành công";
		
		final String DELIVERY_FAIL = "8: Giao hàng không thành công";
		
		final String CANCEL_ORDER = "9: Đơn hàng đã bị huỷ";
		
		final String RETURN_REQUIRE = "10: Yêu cầu đổi trả";
		// final String RETURN_CONFIRM = "11: Xác nhận đổi trả";
		
		List<String> list = new ArrayList<>();
		list.add(WAIT_CONFIRM);
		list.add(AT_STATION);
		list.add(DELIVERY_FAIL);
		list.add(DELIVERY_SUCCESS);
		list.add(DELIVERY_TO_CUS);
		list.add(DELIVERING);
		list.add(SENT_TO_DELIVERY);
		list.add(DELIVERY_FAIL);
		list.add(SENT_TO_DELIVERY);
		list.add(PREPARING);
		list.add(CANCEL_ORDER);
		list.add(RETURN_REQUIRE);
		list.add(PREPARE_TO_DELIVERY);
		String rtnStr = EMPTY;
		for(String s : list) {
			
			List<String> items = Arrays.asList(s.split(":"));
			if (YeeStringUtils.equals(items.get(0), str)) {

				rtnStr = items.get(1);
			}
		}
		
		return rtnStr;
	}
	
	public String format3DigistNumber(Integer num) {
	
		
		return String.format("%03d" ,num);
	}
}
