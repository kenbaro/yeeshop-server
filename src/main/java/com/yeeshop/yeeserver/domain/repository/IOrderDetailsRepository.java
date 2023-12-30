package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.OrderDetails;
import com.yeeshop.yeeserver.domain.entity.Orders;

@Repository
public interface IOrderDetailsRepository extends JpaRepository<OrderDetails, String>{

	public List<OrderDetails> findByOrder(Orders order);
	
	@Query("SELECT o FROM OrderDetails o WHERE o.product <> null AND o.product.productNm = ?1")
	public List<OrderDetails> findByProductNm(String nm);
	
	@Query("SELECT o FROM OrderDetails o WHERE o.product <> null AND o.product.SKU = ?1")
	public List<OrderDetails> findByProductId(String id);
}
