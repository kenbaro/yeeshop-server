package com.yeeshop.yeeserver.domain.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.modelmapper.internal.bytebuddy.asm.Advice.This;
@UtilityClass
public class YeeDateTimeUtils {

	public String convertCurrentTimeStampToStringOnlyNumber() {
		
		long timestamp = System.currentTimeMillis();
		
		return String.valueOf(timestamp);
		
	}
	
	public String changeDateToYmdMinus(String date) {
		
		return YeeStringUtils.isNoneEmpty(date) ? date.replaceAll("/", "-") : YeeStringUtils.EMPTY;
	}
	
	public String convertDateTimeToStringOnlyNumber(String dtStr) {
		
		String numericDateTime = YeeStringUtils.EMPTY;
		if (YeeStringUtils.isNotEmpty(dtStr)) {
			
			// Define the format of the input string
	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	        try {
	            // Parse the input string into a Date object
	            Date date = inputFormat.parse(dtStr);

	            // Define the format for the output string (numbers only)
	            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	            // Format the Date object to get a string with only numbers
	            numericDateTime = outputFormat.format(date);

	            
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		}
		return numericDateTime;
	}

	public String getCurrentDateTime() {
		
		// Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a format for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time using the specified format
        String formattedDateTime = currentDateTime.format(formatter);
        
        return formattedDateTime;
	}
	
	public String getCurrentDate() {
			
		// Get the current date and time
        LocalDateTime currentDate = LocalDateTime.now();

        // Define a format for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date and time using the specified format
        String formattedDate = currentDate.format(formatter);
        
        return formattedDate;
	}
	
	public long calcMinute(String str) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String currentDtime = getCurrentDateTime();
		
		try {
            Date date1 = format.parse(str);
            Date date2 = format.parse(currentDtime);

            long timeDifferenceMillis = date2.getTime() - date1.getTime();
            long minutesDifference = timeDifferenceMillis / (60 * 1000);
            
            return minutesDifference;

        } catch (ParseException e) {
            return -1;
        }
	}
	
	private static SimpleDateFormat YMD_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");
	
	public Boolean validateDateMinus(String dateStr) {
		
		Boolean validDate = true;
		
		if (dateStr == null || "".equals(dateStr.trim())) {
            return false;
        }
        try {
        	
            YMD_FORMAT.parse(dateStr);
        } catch (ParseException e) {

        	validDate = false;
        }
		
		return validDate;
	}
	
	public long calcDaysBetween2Dates(String startdDate, String endDate) {
		
		// Formatter date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // startDate
        LocalDate startdDateLocal = LocalDate.parse(startdDate, formatter);

        // endDate
        LocalDate endDateLocal = LocalDate.parse(endDate, formatter);

        // calc date
        long dayCnt = ChronoUnit.DAYS.between(startdDateLocal, endDateLocal);

        return dayCnt;
	}
	
	public long calcMonthsBetween2YearMonth(String startdDate, String endDate) {
		
		// Formatter date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // startDate
        YearMonth startdDateLocal = YearMonth.parse(startdDate, formatter);

        // endDate
        YearMonth endDateLocal = YearMonth.parse(endDate, formatter);

        // calc date
        long dayCnt = ChronoUnit.MONTHS.between(startdDateLocal, endDateLocal);

        return dayCnt;
	}
	
	public long calcDaysUntilNow(String date) {
		
		// Formatter date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // startDate
        LocalDate dateLocal = LocalDate.parse(date, formatter);

        // endDate
        LocalDate currentDateTime = LocalDate.now();


        // calc date
        long dayCnt = ChronoUnit.DAYS.between(dateLocal, currentDateTime);

        return dayCnt;
	}
	
	public long calcYearsUntilNow(String date) {
		
		// Formatter date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // startDate
        LocalDate dateLocal = LocalDate.parse(date, formatter);

        // endDate
        LocalDate currentDateTime = LocalDate.now();


        // calc date
        long yearCnt = ChronoUnit.YEARS.between(dateLocal, currentDateTime);

        return yearCnt;
	}
	
	public String convertYmdhms2YmdMinus(String inputDate) throws ParseException {
		
		// Define input and output date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Parse the input string to a Date object
        Date date = inputFormat.parse(inputDate);

        // Format the Date object to the desired output format
        String outputDateString = outputFormat.format(date);
        
        return outputDateString;
	}
	
	public String convertYmdhms2YmMinus(String inputDate) throws ParseException {
		
		// Define input and output date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM");
        
        // Parse the input string to a Date object
        Date date = inputFormat.parse(inputDate);

        // Format the Date object to the desired output format
        String outputDateString = outputFormat.format(date);
        
        return outputDateString;
	}
	
	public String convertYmdhms2Year(String inputDate) throws ParseException {
		
		// Define input and output date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy");
        
        // Parse the input string to a Date object
        Date date = inputFormat.parse(inputDate);

        // Format the Date object to the desired output format
        String outputDateString = outputFormat.format(date);
        
        return outputDateString;
	}
	
	public String getMonthYmdMinus(String ymd) {
		
		// Specify the date format of the input string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String month = "";
        try {
            // Parse the date string into a Date object
            Date date = dateFormat.parse(ymd);

            // Use another SimpleDateFormat to extract the month from the Date object
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            month = monthFormat.format(date);

            return month;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month;
	}
	
	public String getMonthYmdhhmmss(String ymdhms) {
		
		// Specify the date format of the input string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String month = "";
        try {
            // Parse the date string into a Date object
            Date date = dateFormat.parse(ymdhms);

            // Use another SimpleDateFormat to extract the month from the Date object
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            month = monthFormat.format(date);

            return month;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month;
	}

	public int getCurrentYear() {
		
		return LocalDate.now().getYear();
	}
	
	public int getCurrentMonth() {
		
		return LocalDate.now().getMonthValue();
	}
	
	public int getCurrentDayInCurrentMonth() {
		
		return LocalDate.now().getDayOfMonth();
	}
	
	public int getCurrentDayInCurrentYear() {
		
		return LocalDate.now().getDayOfYear();
	}
//	public Boolean chkIsAfter(String date) {
//		
//		// Formatter date
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // startDate
//        LocalDate dateLocal = LocalDate.parse(date, formatter);
//	}
}
