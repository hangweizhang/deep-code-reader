package com.code.deepreader.semantic.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.code.deepreader")
public class SemanticServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemanticServiceApplication.class, args);
    }
}
