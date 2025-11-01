package com.upc.dentify.practicemanagementservice.application.internal.outboundservices;

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
    private final String servicePerClinicUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalServicePerClinicService(RestTemplate restTemplate,
                               @Value("${external.servicePerClinic.url}") String servicePerClinicUrl) {
        this.restTemplate = restTemplate;
        this.servicePerClinicUrl = servicePerClinicUrl;
    }

    public Boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId) {
        String url = servicePerClinicUrl + "/api/v1/acl/service-per-clinic/clinic/" + clinicId + "/service/" + serviceId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Boolean.class
        );
        return response.getBody() != null && response.getBody();
    }
}
