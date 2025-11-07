package com.upc.dentify.appointmentservice.application.internal.outboundservices;

import com.upc.dentify.appointmentservice.interfaces.rest.dtos.ShiftResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class ExternalShiftService {
    private final RestTemplate restTemplate;
    private final String clinicManagementBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalShiftService(
            RestTemplate restTemplate,
            @Value("${external.clinicmanagement.url}") String clinicManagementBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.clinicManagementBaseUrl = clinicManagementBaseUrl;
    }

    public List<ShiftResource> getAllShiftsByClinicId(Long clinicId) {
        String url = clinicManagementBaseUrl + "/api/v1/acl/shift/clinic/" + clinicId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ShiftResource[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                ShiftResource[].class
        );
        return List.of(Objects.requireNonNull(response.getBody()));
    }
}
