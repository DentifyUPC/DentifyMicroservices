package com.upc.dentify.appointmentservice.application.internal.outboundservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalServicePerClinicService {
    private final RestTemplate restTemplate;
    private final String clinicManagementBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalServicePerClinicService(RestTemplate restTemplate,
                                           @Value("${external.clinicmanagement.url}") String clinicManagementBaseUrl) {
        this.restTemplate = restTemplate;
        this.clinicManagementBaseUrl = clinicManagementBaseUrl;
    }

    public Double getTotalServicePrice(Long clinicId, Long serviceId) {
        String url = clinicManagementBaseUrl + "/api/v1/acl/service-per-clinic/clinic/" + clinicId + "/service/" + serviceId + "/price";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Double> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Double.class
        );
        return response.getBody();
    }
}
