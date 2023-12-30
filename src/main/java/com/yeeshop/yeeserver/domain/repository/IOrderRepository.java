package com.yeeshop.yeeserver.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Orders;
import com.yeeshop.yeeserver.domain.entity.User;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, String>{

	List<Orders> findByUser(User user);
	
	@Query("SELECT s FROM Orders s WHERE s.status = 0 OR s.status = 1 OR s.status = 2")
	List<Orders> findByUndeliverOrder();
	
	@Query("SELECT s FROM Orders s WHERE s.status = 7")
	List<Orders> findBySuccessOrder();
}
