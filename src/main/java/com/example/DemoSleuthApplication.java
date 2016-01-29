package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class DemoSleuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSleuthApplication.class, args);
	}

}
