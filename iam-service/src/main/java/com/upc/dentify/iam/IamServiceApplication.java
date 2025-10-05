package com.upc.dentify.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing

//@EntityScan(basePackages = "com.upc.dentify.iam.domain.model")
//@EnableJpaRepositories(basePackages = "com.upc.dentify.iam.infrastructure.persistence.jpa.repositories")


public class IamServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IamServiceApplication.class, args);
    }

}
