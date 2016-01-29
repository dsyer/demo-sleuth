package com.example;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableHystrix
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

}
