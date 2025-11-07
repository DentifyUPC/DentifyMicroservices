package com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamnesisRepository extends JpaRepository<Anamnesis, Long> {
}
