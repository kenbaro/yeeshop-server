package com.yeeshop.yeeserver.domain.service;

import java.util.List;

import com.yeeshop.yeeserver.domain.dto.sale.SaleManageDto;
import com.yeeshop.yeeserver.domain.entity.Sales;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import jakarta.servlet.http.HttpServletRequest;

public interface ISaleService {
	
	public List<Sales> getAllSalesExcept(HttpServletRequest request);
	
	public List<Sales> getAllSales();
	
	public List<Sales> filterSales(HttpServletRequest request);
	
	public Boolean addSales(SaleManageDto saleManageDto, YeeMessage yeeMessage);
	
	public Boolean updateSales(SaleManageDto saleManageDto, YeeMessage yeeMessage);
	
	public void getSaleById(SaleManageDto saleManageDto, HttpServletRequest request);
	
	public Boolean updateStatus(HttpServletRequest request, YeeMessage yeeMessage);
	
	public Boolean deleteSale(HttpServletRequest request, YeeMessage yeeMessage);
}
