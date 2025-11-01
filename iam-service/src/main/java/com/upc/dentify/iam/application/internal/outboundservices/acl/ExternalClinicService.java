package com.upc.dentify.iam.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalClinicService {
    private final RestTemplate restTemplate;
    private final String clinicServiceUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalClinicService(RestTemplate restTemplate,
                               @Value("${external.clinic-management.url}") String clinicServiceUrl) {
        this.restTemplate = restTemplate;
        this.clinicServiceUrl = clinicServiceUrl;
    }

    public Boolean existsByClinicId(Long id) {
        String url = clinicServiceUrl + "/api/v1/acl/clinic/" + id;
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
