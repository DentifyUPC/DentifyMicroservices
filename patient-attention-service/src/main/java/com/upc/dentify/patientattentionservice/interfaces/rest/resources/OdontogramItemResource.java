package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record OdontogramItemResource(Long id,
                                     Long teethCode,
                                     String toothStatusName,
                                     Long odontogramId) {
}
