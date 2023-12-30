package com.yeeshop.yeeserver.domain.constant;

/**
 * A common Constants class
 * @author Thai Duy Bao
 * since 2023
 */
public class YeeCommonConst {
	
	/** Unauthorized user. */
	public final static String ANONYMOUS = "anonymousUser";
	
	/***/
	public final static String SHOP_TITLE = "XiaoYi Shop";
	
	/** Roles. */
	public static class YeeRole {
		
		/** Admin Role. */
		public static String ADMIN_ROLE = "ADMIN";
		
		/** User Role. */
		public static String USER_ROLE = "USER";
	}

	public static class YeeError {
		
		/** ERROR_TYPE. */
		public static Integer ERROR = 0;
		
		/** WARNING_TYPE. */
		public static Integer WARNING = 1;
		
		/** INFO_TYPE. */
		public static Integer INFO = 2;
		
		/** SUCCESS_TYPE. */
		public static Integer SUCCESS = 3;
	}
	
	public static class YeeNumber {
		
		/** Number 0. */
		public static Integer INT_0 = 0;
		
		/** Number 1. */
		public static Integer INT_1 = 1;
		
		/** Number 2. */
		public static Integer INT_2 = 2;
		
		/** Number 3. */
		public static Integer INT_3 = 3;
		
		/** Number 4. */
		public static Integer INT_4 = 4;
		
		/** Number 5. */
		public static Integer INT_5 = 5;
		
		/** Number 6. */
		public static Integer INT_6 = 6;
		
		/** Number 7. */
		public static Integer INT_7 = 7;
		
		/** Number 8. */
		public static Integer INT_8 = 8;
		
		/** Number 0. */
		public static Integer INT_9 = 9;
		
		/** Number 10. */
		public static Integer INT_10 = 10;
		
		/** Number 20. */
		public static Integer INT_20 = 20;
		
		/** Number 100. */
		public static Integer INT_100 = 100;
	} 
	
	public static class YeeString {
		
		/** EMPTY String. */
		public static String EMPTY = "";
		
		/** SEPARATE_COMMA*/
		public static String SEPARATE_COMMA = ", ";
		
		/** SEPARATE_SPACE */
		public static String SEPARATE_SPACE = " ";
		
		/** SEPARATE_DASH*/
		public static String SEPARATE_DASH = " - ";
	}

	public static class YeeBannerType {
		
		/** Large Banner*/
		public static String BANNER_HEADER = "BNTYPE01";
		
		/** Large Banner*/
		public static String BANNER_LARGE = "BNTYPE02";
		
		/** Small Right Banner*/
		public static String BANNER_SMALL_R = "BNTYPE04";
		
		/** Long Banner */
		public static String BANNER_LONG = "BNTYPE03";
		
		/** Half Long Banner */
		public static String BANNER_HALF_LONG = "BNTYPE05";
	}
	
	public static class AddressType {
		
		/** Main Location of Shop*/
		public static Integer SHOP_MAIN_LOCATION = 1;
		
		/** Branch of shop*/
		public static Integer SHOP_BRANCH_LOCATION = 2;
		
		/** Home Address of User*/
		public static Integer HOME_ADDRESS = 3;
		
		/** OFFICE ADDRESS of User */
		public static Integer OFFICE_ADDRESS = 4;
		
	}
	
	public static class SALE {

		/** Sale off*/
		public static Integer SALE_OFF = 1;
		
		/** Voucher*/
		public static Integer VOUCHER = 2;
	}
	
	public static class CART {
		
		/** cart */
		public static String CART_CD = "yeeCart_";
		
		/** order */
		public static String ORDER_CD = "yeeOrder_";
		public static String ORDER_DETAIL_CD = "yeeODtls_";
		
	}
	
	public static class PAYMENT {
		
		/** VNPAY code */
		public static String VNPAY_CD = "VnPay_PAYMENT";
		
		/** Default */
		public static String PAY_CD = "PAY01";
		
	}
	
	public static class ORDER_STATUS {
		
		/** WAIT_CONFIRM */
		public static String WAIT_CONFIRM = "0";
		
		/** PREPARING */
		public static String PREPARING = "1";
		
		/** SENT_TO_DELIVERY */
		public static String SENT_TO_DELIVERY = "2";
		
		/** DELIVERING */
		public static String DELIVERING = "3";
		
		/** AT_STATION */
		public static String AT_STATION = "4";
		
		/** AT_STATION */
		public static String DELIVERY_TO_CUS = "5";
		
		public static String DELIVERY_SUCCESS = "6";
		
		public static String DELIVERY_FAIL = "7";
		
		public static String PAY_SUCCESS = "8";
	}
}
