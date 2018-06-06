package com.fafc.bet4fun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { 
        SecurityAutoConfiguration.class
})
public class Bet4funApplication {

	public static void main(String[] args) {
		SpringApplication.run(Bet4funApplication.class, args);
	}
}
