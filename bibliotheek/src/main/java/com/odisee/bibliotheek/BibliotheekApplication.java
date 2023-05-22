package com.odisee.bibliotheek;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@EnableCaching
@Slf4j
public class BibliotheekApplication {

	public static void main(String[] args) {

		SpringApplication.run(BibliotheekApplication.class, args);
		log.info("Running REST API with request caching.");
	}

}
