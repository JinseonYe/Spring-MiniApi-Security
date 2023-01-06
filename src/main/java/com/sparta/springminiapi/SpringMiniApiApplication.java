package com.sparta.springminiapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringMiniApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMiniApiApplication.class, args);
    }

}
