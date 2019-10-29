package com.rcm.app.dbConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbMockAtcmaApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DbMockAtcmaApplication.class);
	
	public static void main(String[] args) {
		LOGGER.info("Booting up the application");
		
		SpringApplication.run(DbMockAtcmaApplication.class, args);
		
	}
	
}
