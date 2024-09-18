package com.lorcan.bet.asyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class InventoryAndOrderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryAndOrderManagementApplication.class, args);
	}

}
