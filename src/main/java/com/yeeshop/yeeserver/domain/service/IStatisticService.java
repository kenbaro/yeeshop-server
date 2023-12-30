package com.yeeshop.yeeserver.domain.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.yeeshop.yeeserver.domain.dto.statistic.DateInComeDto;
import com.yeeshop.yeeserver.domain.dto.statistic.IncomeCurrentYearDto;
import com.yeeshop.yeeserver.domain.dto.statistic.ProductAnalysisDto;

import jakarta.servlet.http.HttpServletRequest;

public interface IStatisticService {

	public HashMap<String, Object> getTotalInCome();
	
	public IncomeCurrentYearDto getInComeCurrentYear();
	
	public List<DateInComeDto> getIncomeDate(HttpServletRequest request) throws ParseException;
	
	public List<ProductAnalysisDto> getProductAnalysis();
	
}
