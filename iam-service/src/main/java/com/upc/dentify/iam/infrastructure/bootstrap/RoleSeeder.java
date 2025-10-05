package com.upc.dentify.iam.infrastructure.bootstrap;

import com.upc.dentify.iam.domain.model.entities.Role;
import com.upc.dentify.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<String> roles = List.of("ADMIN", "ODONTOLOGIST", "PATIENT");

        for (String roleName : roles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }
}