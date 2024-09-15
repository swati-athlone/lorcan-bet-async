package com.lorcan.bet.asyn.repository;

import com.lorcan.bet.asyn.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
