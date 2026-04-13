package com.codexp.solutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SolutionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolutionsApplication.class, args);
    }

}
