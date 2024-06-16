package com.example.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Value("${test.code}")
    String test;
    @Value("${application.test}")
    String root;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(test);
    }
}
