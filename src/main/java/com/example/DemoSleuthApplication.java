package com.example;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableHystrix
@EnableAsync
public class DemoSleuthApplication {

	@Bean
	public AsyncConfigurer asyncConfigurer() {

		return new AsyncConfigurer() {
			@Override
			public Executor getAsyncExecutor() {
				ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
				taskExecutor.setCorePoolSize(5);
				taskExecutor.setMaxPoolSize(10);
				taskExecutor.setQueueCapacity(25);
				taskExecutor.setThreadNamePrefix("custom-");
				taskExecutor.initialize();
				return taskExecutor;
			}

			@Override
			public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
				return new SimpleAsyncUncaughtExceptionHandler();
			}

		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoSleuthApplication.class, args);
	}

}
