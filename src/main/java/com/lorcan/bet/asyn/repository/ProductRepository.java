package com.lorcan.bet.asyn.repository;

import com.lorcan.bet.asyn.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
