package com.yeeshop.yeeserver.domain.repository;

import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.ShopInfo;

public interface IHeaderRepository {

	public ShopInfo getShopInfo();
	
	public Banner getHeaderBanner();
}
