package com.yeeshop.yeeserver.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeeshop.yeeserver.domain.entity.DeliveryUnit;

@Repository
public interface IDeliveryUnitRepository extends JpaRepository<DeliveryUnit, String>{

}
