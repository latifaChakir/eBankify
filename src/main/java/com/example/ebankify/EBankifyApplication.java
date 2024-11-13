package com.example.ebankify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EBankifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankifyApplication.class, args);
    }

}
