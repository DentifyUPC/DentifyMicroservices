package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ToothStatus;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllToothStatusQuery;
import com.upc.dentify.patientattentionservice.domain.services.ToothStatusQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ToothStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToothStatusQueryServiceImpl implements ToothStatusQueryService {

    private final ToothStatusRepository toothStatusRepository;

    public ToothStatusQueryServiceImpl(ToothStatusRepository toothStatusRepository) {
        this.toothStatusRepository = toothStatusRepository;
    }

    @Override
    public List<ToothStatus> handle(GetAllToothStatusQuery query) {
        return toothStatusRepository.findAll();
    }
}
