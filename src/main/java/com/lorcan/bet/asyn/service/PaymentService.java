package com.lorcan.bet.asyn.service;

import com.lorcan.bet.asyn.entity.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Async
public class PaymentService {

    @Async
    public CompletableFuture<Boolean> processPayment(Order order) {
        // Simulate a random payment failure
        boolean paymentSuccess = new Random().nextBoolean();
        return CompletableFuture.completedFuture(paymentSuccess);
    }
}
