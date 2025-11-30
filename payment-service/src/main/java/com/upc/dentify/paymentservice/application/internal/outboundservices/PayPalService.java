package com.upc.dentify.paymentservice.application.internal.outboundservices;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
public class PayPalService {

    private final PayPalHttpClient payPalHttpClient;
    
    @Value("${paypal.currency:USD}")
    private String currency;
    
    @Value("${paypal.return-url:http://localhost:3000/payment/success}")
    private String returnUrl;
    
    @Value("${paypal.cancel-url:http://localhost:3000/payment/cancel}")
    private String cancelUrl;

    public PayPalService(
            @Value("${paypal.client-id}") String clientId,
            @Value("${paypal.client-secret}") String clientSecret,
            @Value("${paypal.mode:sandbox}") String mode) {
        
        PayPalEnvironment environment;
        if ("live".equalsIgnoreCase(mode)) {
            environment = new PayPalEnvironment.Live(clientId, clientSecret);
        } else {
            environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        }
        
        this.payPalHttpClient = new PayPalHttpClient(environment);
    }

    public Order createOrder(Double amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        String formattedAmount = decimalFormat.format(amount);

        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown()
                .currencyCode(currency)
                .value(formattedAmount);

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(amountBreakdown);

        orderRequest.purchaseUnits(Arrays.asList(purchaseUnitRequest));

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);
        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.requestBody(orderRequest);

        HttpResponse<Order> response = payPalHttpClient.execute(request);
        return response.result();
    }

    public Order captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        HttpResponse<Order> response = payPalHttpClient.execute(request);
        return response.result();
    }

    public Order getOrder(String orderId) throws IOException {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = payPalHttpClient.execute(request);
        return response.result();
    }
}
