package com.lorcan.bet.asyn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private static Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final Random random = new Random();

    @Async
    public CompletableFuture<Boolean> processPayment(Long orderId, Double amount) throws InterruptedException {
        // Simulate a delay to mimic network or external API delay
        TimeUnit.SECONDS.sleep(2);

        // Simulate random outcomes: success, failure, or timeout
        int outcome = random.nextInt(3); // Random number 0, 1, or 2

        switch (outcome) {
            case 0: // Success
                log.info("Payment succeeded for order: {}",orderId);
                return CompletableFuture.completedFuture(true);
            case 1: // Failure
                log.info("Payment failed for order: {}",orderId);
                return CompletableFuture.completedFuture(false);
            case 2: // Timeout (simulated by a delay beyond the allowed time)
                log.info("Payment timeout for order: {}",orderId);
                TimeUnit.SECONDS.sleep(5); // Simulating timeout
                return CompletableFuture.completedFuture(false);
            default:
                return CompletableFuture.completedFuture(false);
        }
    }
}

