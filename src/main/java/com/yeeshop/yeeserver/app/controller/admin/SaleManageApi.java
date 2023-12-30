package com.yeeshop.yeeserver.app.controller.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yeeshop.yeeserver.domain.dto.sale.SaleManageDto;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.service.ISaleService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class SaleManageApi {

	@Autowired
	private ISaleService saleService;
	
	@Autowired private ModelMapper mapper;
	
	@GetMapping("/get-all-sale-unexpired-except-saleId")
	public ResponseEntity<?> getAllUnExpiredSale(HttpServletRequest request) {

		List<Sales> sales = this.saleService.getAllSalesExcept(request);
		
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}
	
	@GetMapping("/get-all-sale")
	public ResponseEntity<?> getAllSales() {

		List<Sales> sales = this.saleService.getAllSales();
		
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}
	
	@GetMapping("/filter-sale")
	public ResponseEntity<?> getAllSales(HttpServletRequest request) {

		List<Sales> sales = this.saleService.filterSales(request);
		
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}
	
	@PostMapping("/add-sale")
	public ResponseEntity<?> addSale(@RequestBody SaleManageDto saleForm) {

		SaleManageDto saleManageDto = this.mapper.map(saleForm, SaleManageDto.class);
		YeeMessage yeeMessage = new YeeMessage();
		
		Boolean addOk = this.saleService.addSales(saleManageDto, yeeMessage);
		
		if (!addOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
	@PostMapping("/update-sale")
	public ResponseEntity<?> updateSale(@RequestBody SaleManageDto saleForm) {

		SaleManageDto saleManageDto = this.mapper.map(saleForm, SaleManageDto.class);
		YeeMessage yeeMessage = new YeeMessage();
		
		Boolean updateOk = this.saleService.updateSales(saleManageDto, yeeMessage);
		
		if (!updateOk) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
	@GetMapping("/update-sale-status")
	public ResponseEntity<?> updatePauseOrContinueStt(HttpServletRequest request) {
		
		YeeMessage yeeMessage = new YeeMessage();
		Boolean updateStt = this.saleService.updateStatus(request, yeeMessage);
		
		if (!updateStt) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
	@GetMapping("/delete-sale")
	public ResponseEntity<?> deleteSale(HttpServletRequest request) {
		
		YeeMessage yeeMessage = new YeeMessage();
		Boolean deleteStt = this.saleService.deleteSale(request, yeeMessage);
		
		if (!deleteStt) {
			
			return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(yeeMessage.getMessageTitle(), HttpStatus.OK);
	}
	
	@GetMapping("/get-sale-by-id")
	public ResponseEntity<?> getSaleById(HttpServletRequest request) {

		SaleManageDto saleManageDto = new SaleManageDto(); 
		this.saleService.getSaleById(saleManageDto, request);
		return new ResponseEntity<>(saleManageDto, HttpStatus.OK);
	}
}
