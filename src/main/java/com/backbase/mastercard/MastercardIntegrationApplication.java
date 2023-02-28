package com.backbase.mastercard;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MastercardIntegrationApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(MastercardIntegrationApplication.class, args);
    }

}