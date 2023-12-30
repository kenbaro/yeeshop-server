package com.yeeshop.yeeserver.app.controller.admin;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.domain.dto.statistic.DateInComeDto;
import com.yeeshop.yeeserver.domain.dto.statistic.IncomeCurrentYearDto;
import com.yeeshop.yeeserver.domain.dto.statistic.ProductAnalysisDto;
import com.yeeshop.yeeserver.domain.service.IStatisticService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class StatisticManageApi {
	
	@Autowired private IStatisticService statisticService;

	@GetMapping("/get-order-income")
	public ResponseEntity<?> getAllSales() {

		HashMap<String, Object> map = this.statisticService.getTotalInCome();
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@GetMapping("/get-order-income-current-year")
	public ResponseEntity<?> getInComeCurrentYear() {

		IncomeCurrentYearDto map = this.statisticService.getInComeCurrentYear();
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@GetMapping("/get-date-income")
	public ResponseEntity<?> getDateInCome(HttpServletRequest request) throws ParseException{

		List<DateInComeDto> dateInComeDto = this.statisticService.getIncomeDate(request);
		
		return new ResponseEntity<>(dateInComeDto, HttpStatus.OK);
	}
	
	@GetMapping("/get-product-analysis")
	public ResponseEntity<?> getProductAnalysis(){

		List<ProductAnalysisDto> productAnalysisDtos = this.statisticService.getProductAnalysis();
		
		return new ResponseEntity<>(productAnalysisDtos, HttpStatus.OK);
	}
}
