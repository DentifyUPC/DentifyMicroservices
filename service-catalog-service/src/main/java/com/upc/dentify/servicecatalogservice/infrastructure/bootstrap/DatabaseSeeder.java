package com.upc.dentify.servicecatalogservice.infrastructure.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UnitTypeSeeder unitTypeSeeder;
    private final ItemSeeder itemSeeder;
    private final ServiceSeeder serviceSeeder;
    private final ItemPerServiceSeeder  itemPerServiceSeeder;

    public DatabaseSeeder(UnitTypeSeeder unitTypeSeeder,
                          ItemSeeder itemSeeder,
                          ServiceSeeder serviceSeeder,
                          ItemPerServiceSeeder itemPerServiceSeeder) {
        this.unitTypeSeeder = unitTypeSeeder;
        this.itemSeeder = itemSeeder;
        this.serviceSeeder = serviceSeeder;
        this.itemPerServiceSeeder = itemPerServiceSeeder;
    }

    @Override
    public void run(String... args) {
        unitTypeSeeder.seed();
        itemSeeder.seed();
        serviceSeeder.seed();
        itemPerServiceSeeder.seed();
    }
}

