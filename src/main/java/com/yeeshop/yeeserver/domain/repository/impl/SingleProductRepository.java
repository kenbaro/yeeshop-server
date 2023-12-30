package com.yeeshop.yeeserver.domain.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Product;
import com.yeeshop.yeeserver.domain.repository.ISingleProductRepository;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class SingleProductRepository implements ISingleProductRepository{


	@Autowired 
    SessionFactory factory;

	@Override
	public Product getProductByStorageAndColor(HashMap<String, Object> paramMap) {
		
		String hql = "FROM Product p WHERE p.productStorage =:storage AND p.productColor =:color AND p.isAvailabled = 1";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql,Product.class);
		query.setParameter("storage", paramMap.get("storage"));
		query.setParameter("color", paramMap.get("color"));
		List<Product> products = query.getResultList();
		
		if (0 < products.size()) {
			
			return products.get(0);
		}
		
		return null;
	}


	@Override
	public List<Product> getProductBySeriesId(HashMap<String, Object> paramMap) {
		
		String hql = "FROM Product p WHERE p.productSeries.pSeriesCd =:id";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql,Product.class);
		query.setParameter("id", paramMap.get("id"));
		List<Product> products = query.getResultList();
		
		return products;
	}


	@Override
	public List<Product> getProductBySeriesIdAndStorage(HashMap<String, Object> paramMap) {

		String hql = "FROM Product p WHERE p.productSeries.pSeriesCd =:id AND p.productStorage =:storage";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql,Product.class);
		query.setParameter("id", paramMap.get("id"));
		query.setParameter("storage", paramMap.get("storage"));
		List<Product> products = query.getResultList();
	
		return products;
	}

}
