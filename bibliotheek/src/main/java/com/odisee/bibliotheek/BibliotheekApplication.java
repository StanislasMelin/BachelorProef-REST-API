package com.odisee.bibliotheek;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableCaching
@EnableAsync
@Slf4j
public class BibliotheekApplication {

	public static void main(String[] args) {

		SpringApplication.run(BibliotheekApplication.class, args);
		log.info("Running fully optimized REST API.");
	}

	// Hier maak in een task executor. Deze functie zal gebruikt worden om
	// nieuwe threads op te starten om asynchrone functies uit te voeren.
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("AsyncFileStorage-");
		executor.initialize();
		return executor;
	}
}
