package com.upc.dentify.patientattentionservice.infrastructure.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final TeethSeeder teethSeeder;
    private final ToothStatusSeeder toothStatusSeeder;

    public DatabaseSeeder(TeethSeeder teethSeeder, ToothStatusSeeder toothStatusSeeder) {
        this.teethSeeder = teethSeeder;
        this.toothStatusSeeder = toothStatusSeeder;
    }

    @Override
    public void run(String... args) {
        toothStatusSeeder.seed();
        teethSeeder.seed();
    }
}
