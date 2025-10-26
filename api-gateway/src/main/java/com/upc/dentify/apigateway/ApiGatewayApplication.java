package com.upc.dentify.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiGatewayApplication.class, args);

//		SpringApplication app = new SpringApplication(ApiGatewayApplication.class);
//		// forzar propiedad (sobrescribe application.properties mientras se ejecute)
//		app.setDefaultProperties(Map.of("gateway.security.enabled", "false"));
//		app.run(args);
	}

}
