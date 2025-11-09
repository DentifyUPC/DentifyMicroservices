package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllOdontogramItemsByOdontogramIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.OdontogramItemQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.OdontogramItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontogramItemQueryServiceImpl implements OdontogramItemQueryService {

    private final OdontogramItemRepository odontogramItemRepository;

    public OdontogramItemQueryServiceImpl(OdontogramItemRepository odontogramItemRepository) {
        this.odontogramItemRepository = odontogramItemRepository;
    }

    @Override
    public List<OdontogramItem> handle(GetAllOdontogramItemsByOdontogramIdQuery query) {
        return odontogramItemRepository.findByOdontogram_Id(query.odontogramId());
    }
}
