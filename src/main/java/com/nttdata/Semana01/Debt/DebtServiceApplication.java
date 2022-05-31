package com.nttdata.Semana01.Debt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DebtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebtServiceApplication.class, args);
	}

}
