package com.odisee.bibliotheek;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@Slf4j
public class BibliotheekApplication {

	public static void main(String[] args) {

		SpringApplication.run(BibliotheekApplication.class, args);
		log.info("Running load balanced REST API.");
	}

}
