package com.yeeshop.yeeserver.domain.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yeeshop.yeeserver.domain.entity.Sales;

import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class HibernateQuerrySupportRepository {

	@Autowired 
    SessionFactory factory;

	public List<Sales> findSalesContainingKeyWord(String kw) {
		
		String hql = "FROM Sales s WHERE s.saleNm LIKE :keyword";
		Session session = factory.getCurrentSession();
		TypedQuery<Sales> query = session.createQuery(hql,Sales.class);
		query.setParameter("keyword","%"+ kw+ "%");
		List<Sales> sales = query.getResultList();
		
		return sales;
	}
}
