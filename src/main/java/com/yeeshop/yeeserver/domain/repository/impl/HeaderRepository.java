package com.yeeshop.yeeserver.domain.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Banner;
import com.yeeshop.yeeserver.domain.entity.ShopInfo;
import com.yeeshop.yeeserver.domain.repository.IHeaderRepository;

import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HeaderRepository implements IHeaderRepository{

	@Autowired 
    SessionFactory factory;
	
	@Override
	public ShopInfo getShopInfo() {
		
		String hql = "FROM ShopInfo s where s.shopAddress.adrType = 1";
		Session session=factory.getCurrentSession();
		
		TypedQuery<ShopInfo> query=session.createQuery(hql,ShopInfo.class);
		
		ShopInfo shopInfo = query.getSingleResult();
		
		return shopInfo;
	}

	@Override
	public Banner getHeaderBanner() {

		String hql = "FROM Banner b where b.bannerType.BnTypeCd = 'BNTYPE01'";
		Session session=factory.getCurrentSession();
		
		TypedQuery<Banner> query=session.createQuery(hql,Banner.class);
		
		Banner banner = query.getSingleResult();

		return banner;
	}

	
}
