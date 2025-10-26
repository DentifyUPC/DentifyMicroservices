package com.upc.dentify.practicemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PracticeManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeManagementServiceApplication.class, args);
    }

}
