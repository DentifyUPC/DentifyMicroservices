package com.upc.dentify.practicemanagementservice.application.internal.queryservices;

import com.upc.dentify.practicemanagementservice.domain.model.aggregates.Odontologist;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetAllOdontologistByClinicId;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistById;
import com.upc.dentify.practicemanagementservice.domain.model.queries.GetOdontologistByUserId;
import com.upc.dentify.practicemanagementservice.domain.services.OdontologistQueryService;
import com.upc.dentify.practicemanagementservice.infrastructure.persistence.jpa.repositories.OdontologistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologistQueryServiceImpl implements OdontologistQueryService {

    private final OdontologistRepository odontologistRepository;

    public OdontologistQueryServiceImpl(OdontologistRepository odontologistRepository) {
        this.odontologistRepository = odontologistRepository;
    }


    @Override
    public List<Odontologist> handle(GetAllOdontologistByClinicId query) {
        return odontologistRepository.findByClinicId(query.clinicId());
    }

    @Override
    public Optional<Odontologist> handle(GetOdontologistById query) {
        return odontologistRepository.findById(query.id());
    }

    @Override
    public Optional<Odontologist> handle(GetOdontologistByUserId query) {
        return odontologistRepository.findByUserId(query.userId());
    }
}
