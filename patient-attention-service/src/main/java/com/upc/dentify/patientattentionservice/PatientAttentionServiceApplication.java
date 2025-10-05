package com.upc.dentify.patientattentionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PatientAttentionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientAttentionServiceApplication.class, args);
    }

}
