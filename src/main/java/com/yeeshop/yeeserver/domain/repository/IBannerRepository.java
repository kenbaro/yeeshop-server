package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Banner;

@Repository
public interface IBannerRepository extends JpaRepository<Banner, String>{
	
	List<Banner> findByBannerLink(String bannerLink);
}
