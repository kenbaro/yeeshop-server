package com.yeeshop.yeeserver.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart, String>{

	@Query("SELECT c FROM Cart c WHERE c.cartProduct.SKU = ?1 AND c.cartUser.userCd =?2")
	public Cart findCartByProductId(String pid,String userid);
	
	@Query("SELECT count(c) FROM Cart c WHERE c.cartUser.userCd =?1")
	public Integer countCartByUserCd(String userCd);
	
	@Query("SELECT c FROM Cart c WHERE c.cartUser.userCd =?1")
	public List<Cart> findCartByUserCd(String userCd);
	
	@Query("SELECT c FROM Cart c WHERE c.cartUser.userCd =?1 AND c.cartId =?2")
	public Optional<Cart> findCartofCustomerById(String userid, String cartId);
}
