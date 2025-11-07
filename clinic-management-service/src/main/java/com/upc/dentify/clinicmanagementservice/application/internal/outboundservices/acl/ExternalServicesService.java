package com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ServiceResource;
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
public class ExternalServicesService {
    private final RestTemplate restTemplate;
    private final String serviceCatalogBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalServicesService(RestTemplate restTemplate,
                                   @Value("${external.servicecatalog.url}") String serviceCatalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.serviceCatalogBaseUrl = serviceCatalogBaseUrl;
    }

    public List<ServiceResource> getAllServices() {
        String url = serviceCatalogBaseUrl + "/api/v1/acl/service";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalServiceToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ServiceResource[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                ServiceResource[].class
        );
        return List.of(Objects.requireNonNull(response.getBody()));
    }
}
