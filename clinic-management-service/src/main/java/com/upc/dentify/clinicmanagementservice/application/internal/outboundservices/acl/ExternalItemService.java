package com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalItemService {

    private final RestTemplate restTemplate;
    private final String serviceCatalogBaseUrl;

    public ExternalItemService(RestTemplate restTemplate,
                               @Value("${external.servicecatalog.url}") String serviceCatalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.serviceCatalogBaseUrl = serviceCatalogBaseUrl;
    }

    public Boolean existsById(Long id) {
        String url = serviceCatalogBaseUrl + "/api/v1/acl/items" + id + "/exists";
        Boolean response = restTemplate.getForObject(url, Boolean.class);
        return response != null && response;
    }
}
