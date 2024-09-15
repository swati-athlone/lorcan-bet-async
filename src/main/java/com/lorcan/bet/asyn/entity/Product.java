package com.lorcan.bet.asyn.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String name;
    private String description;
    private Double price;

}
