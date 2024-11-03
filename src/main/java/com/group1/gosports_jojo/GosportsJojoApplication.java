package com.group1.gosports_jojo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GosportsJojoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GosportsJojoApplication.class, args);
    }

}
