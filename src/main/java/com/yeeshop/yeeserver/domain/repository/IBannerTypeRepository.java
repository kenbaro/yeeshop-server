package com.yeeshop.yeeserver.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.BannerType;

@Repository
public interface IBannerTypeRepository extends JpaRepository<BannerType, String>{

}
