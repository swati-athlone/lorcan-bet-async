package com.lorcan.bet.asyn.repository;

import com.lorcan.bet.asyn.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId")
    Inventory findByProductIdWithLock(@Param("productId") Long productId);

    Optional<Inventory> findByProductId(Long productId);

}
