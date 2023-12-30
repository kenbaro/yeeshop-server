package com.yeeshop.yeeserver.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.ShopInfo;

@Repository
public interface IShopInfoRepository extends JpaRepository<ShopInfo, String>{

}
