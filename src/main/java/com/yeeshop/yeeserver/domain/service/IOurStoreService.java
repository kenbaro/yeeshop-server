package com.yeeshop.yeeserver.domain.service;

import com.yeeshop.yeeserver.domain.dto.ourstore.StoreDto;

public interface IOurStoreService {

	public void initStore(StoreDto storeDto, String keyWord);
	
	//public void filterProduct(StoreDto storeDto);
	
	public StoreDto setFilterCondition(StoreDto storeDto, String br, String fromPr, String toPr, String filterBy, Boolean showMore);
	
	public void filterProductv2(StoreDto storeDto);
}
