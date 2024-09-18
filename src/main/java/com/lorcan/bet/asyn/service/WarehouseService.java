package com.lorcan.bet.asyn.service;

import com.lorcan.bet.asyn.dto.WarehouseInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class WarehouseService {

    private final WebClient webClient;

    public WarehouseService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    /*public List<WarehouseInventoryDto> fetchWarehouseInventory() {
        try {
            Mono<List<WarehouseInventoryDto>> response = webClient.get()
                    .uri("/inventory")
                    .retrieve()
                    .bodyToMono((Class<List<WarehouseInventoryDto>>) (Object) List.class);

            return response.block(); // Blocking since this is a scheduled task
        } catch (Exception e) {
            log.error("Error fetching warehouse inventory: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch warehouse inventory");
        }
    }*/

    public List<WarehouseInventoryDto> fetchWarehouseInventory() {
        return this.webClient
                .get()
                .uri("/api/inventory")
                .retrieve()
                .bodyToFlux(WarehouseInventoryDto.class)
                .collectList()  // This collects the Flux into a Mono<List<WarehouseInventoryDto>>
                .block();       // This blocks the thread and retrieves the actual List
    }
}