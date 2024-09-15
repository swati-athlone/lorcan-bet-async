package com.lorcan.bet.asyn.entity;

import com.lorcan.bet.asyn.util.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_logs")
@Data
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime processedAt;

    private String errorMessage;
}
