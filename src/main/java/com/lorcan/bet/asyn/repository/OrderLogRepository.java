package com.lorcan.bet.asyn.repository;

import com.lorcan.bet.asyn.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
}
