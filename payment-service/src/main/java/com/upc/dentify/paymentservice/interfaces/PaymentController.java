package com.upc.dentify.paymentservice.interfaces;

import com.paypal.orders.Order;
import com.upc.dentify.paymentservice.application.internal.outboundservices.PayPalService;
import com.upc.dentify.paymentservice.domain.model.aggregates.Payment;
import com.upc.dentify.paymentservice.domain.model.command.ProcessPaymentCommand;
import com.upc.dentify.paymentservice.domain.model.command.UpdatePaymentAmountCommand;
import com.upc.dentify.paymentservice.domain.model.queries.*;
import com.upc.dentify.paymentservice.domain.services.PaymentCommandService;
import com.upc.dentify.paymentservice.domain.services.PaymentQueryService;
import com.upc.dentify.paymentservice.interfaces.rest.assemblers.PaymentResourceFromEntityAssembler;
import com.upc.dentify.paymentservice.interfaces.rest.dtos.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment Management Endpoints")
public class PaymentController {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;
    private final PayPalService payPalService;

    public PaymentController(
            PaymentCommandService paymentCommandService,
            PaymentQueryService paymentQueryService,
            PayPalService payPalService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
        this.payPalService = payPalService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/amount")
    public ResponseEntity<PaymentResource> updatePaymentAmount(
            @PathVariable Long id,
            @RequestBody UpdatePaymentAmountRequest request) {
        try {
            var command = new UpdatePaymentAmountCommand(id, request.amount());
            var payment = paymentCommandService.handle(command);
            
            if (payment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var resource = PaymentResourceFromEntityAssembler.fromEntityToResource(payment.get());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @PostMapping("/paypal/create-order")
    public ResponseEntity<?> createPayPalOrder(@RequestBody CreatePayPalOrderRequest request) {
        try {
            var paymentQuery = new GetPaymentByIdQuery(request.paymentId());
            var payment = paymentQueryService.handle(paymentQuery);
            
            if (payment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            if (payment.get().getAmount() <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Payment amount must be set before creating PayPal order"));
            }
            
            Order order = payPalService.createOrder(payment.get().getAmount());
            
            return ResponseEntity.ok(new PayPalOrderResponse(
                    order.id(),
                    order.status(),
                    order.links().stream()
                            .filter(link -> "approve".equals(link.rel()))
                            .findFirst()
                            .map(link -> link.href())
                            .orElse(null)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error creating PayPal order: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @PostMapping("/paypal/capture-order")
    public ResponseEntity<?> capturePayPalOrder(@RequestBody CapturePayPalOrderRequest request) {
        try {
            var command = new ProcessPaymentCommand(request.paymentId(), request.orderId());
            var payment = paymentCommandService.handle(command);
            
            if (payment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var resource = PaymentResourceFromEntityAssembler.fromEntityToResource(payment.get());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Error capturing PayPal order: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentResource>> getAllPayments() {
        var query = new GetAllPaymentsQuery();
        var payments = paymentQueryService.handle(query);
        var resources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::fromEntityToResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long id) {
        var query = new GetPaymentByIdQuery(id);
        var payment = paymentQueryService.handle(query);
        
        if (payment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = PaymentResourceFromEntityAssembler.fromEntityToResource(payment.get());
        return ResponseEntity.ok(resource);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT')")
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResource> getPaymentByAppointmentId(@PathVariable Long appointmentId) {
        var query = new GetPaymentByAppointmentIdQuery(appointmentId);
        var payment = paymentQueryService.handle(query);
        
        if (payment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = PaymentResourceFromEntityAssembler.fromEntityToResource(payment.get());
        return ResponseEntity.ok(resource);
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentResource>> getPaymentsByPatientId(@PathVariable Long patientId) {
        var query = new GetPaymentsByPatientIdQuery(patientId);
        var payments = paymentQueryService.handle(query);
        var resources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::fromEntityToResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    record PayPalOrderResponse(String orderId, String status, String approvalUrl) {}
    record ErrorResponse(String message) {}
}
