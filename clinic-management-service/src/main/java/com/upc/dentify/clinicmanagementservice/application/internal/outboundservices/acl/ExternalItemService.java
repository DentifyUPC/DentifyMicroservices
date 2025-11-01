package com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
public class ExternalItemService {

    private final RestTemplate restTemplate;
    private final String serviceCatalogBaseUrl;
    @Value("${internal.service.token}")
    private String internalServiceToken;

    public ExternalItemService(RestTemplate restTemplate,
                               @Value("${external.servicecatalog.url}") String serviceCatalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.serviceCatalogBaseUrl = serviceCatalogBaseUrl;
    }

    public Boolean existsById(Long id) {
        String url = serviceCatalogBaseUrl + "/api/v1/acl/items/" + id + "/exists";
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
