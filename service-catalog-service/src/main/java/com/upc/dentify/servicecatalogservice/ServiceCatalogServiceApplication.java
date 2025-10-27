package com.upc.dentify.servicecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServiceCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCatalogServiceApplication.class, args);
    }

}
