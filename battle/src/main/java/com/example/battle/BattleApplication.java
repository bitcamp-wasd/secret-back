package com.example.battle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class BattleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleApplication.class, args);
    }

}
