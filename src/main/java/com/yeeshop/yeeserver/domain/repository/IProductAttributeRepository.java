package com.yeeshop.yeeserver.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.ProductAttribute;

@Repository
public interface IProductAttributeRepository extends JpaRepository<ProductAttribute, String>{

}
