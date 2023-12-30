package com.yeeshop.yeeserver.domain.repository.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.entity.ProductSeries;
import com.yeeshop.yeeserver.domain.repository.IHomeRepository;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.hibernate.Session;

@Repository
@Transactional
public class HomeRepository implements IHomeRepository{

	@Autowired 
    SessionFactory factory;
	
	@Override
	public List<ProductSeries> getProductSeriesBySubCateId(final String sid) {
		
		String hql = "FROM ProductSeries pr WHERE pr.subCategory.subCateCd =:sid";
		Session session = factory.getCurrentSession();
		TypedQuery<ProductSeries> query = session.createQuery(hql,ProductSeries.class);
		query.setParameter("sid", sid);
		List<ProductSeries> productSeries = query.getResultList();
		
		return productSeries;
	}

	@Override
	public List<Product> getProductsByPSeriesIds(List<String> pramMap) {

		String hql = "FROM Product p WHERE p.productSeries.pSeriesCd IN (:params) group by p.productNm";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql,Product.class);
		query.setParameter("params", pramMap);
		query.setMaxResults(12);
		List<Product> products = query.getResultList();
		
		return products;
		
	}


}
