package com.upc.dentify.iam.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Long> {
    Optional<IdentificationType> findByName(String name);
    Optional<IdentificationType> findById(Long id);
}
