package com.example;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import java.util.concurrent.Executor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableHystrix
@EnableAsync
public class DemoSleuthApplication {

    protected final Log logger = LogFactory.getLog(getClass());

    public static void main(String[] args) {
        SpringApplication.run(DemoSleuthApplication.class, args);
    }

    @Bean
    public Sampler<?> defaultSampler() {
        return new AlwaysSampler();
    }

    // Use this for debugging (or if there is no Zipkin collector running on port 9410)
    @Bean
    @ConditionalOnProperty(value="sample.zipkin.enabled", havingValue="false")
    public SpanCollector spanCollector() {
        return new LoggingSpanCollector();
    }

    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurer() {
            @Override public Executor getAsyncExecutor() {
                ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
                taskExecutor.setCorePoolSize(5);
                taskExecutor.setMaxPoolSize(10);
                taskExecutor.setQueueCapacity(25);
                taskExecutor.setThreadNamePrefix("ThreadPoolTaskExecutor-");
                taskExecutor.initialize();
                return taskExecutor;
            }

            @Override public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return new SimpleAsyncUncaughtExceptionHandler();
            }
        };
    }
}
