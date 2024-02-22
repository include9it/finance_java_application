package io.include9it.finance_java_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinanceJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceJavaApplication.class, args);
	}

}
