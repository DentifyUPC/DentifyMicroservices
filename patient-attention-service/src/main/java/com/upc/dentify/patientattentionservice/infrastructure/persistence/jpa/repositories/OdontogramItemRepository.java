package com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OdontogramItemRepository extends JpaRepository<OdontogramItem, Long> {
    List<OdontogramItem> findByOdontogram_Id(Long odontogramId);
}
