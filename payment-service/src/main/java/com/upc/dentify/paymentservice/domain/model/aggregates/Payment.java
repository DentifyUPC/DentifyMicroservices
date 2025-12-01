package com.upc.dentify.paymentservice.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upc.dentify.paymentservice.domain.model.command.CreatePaymentCommand;
import com.upc.dentify.paymentservice.domain.model.valueobjects.PaymentState;
import com.upc.dentify.paymentservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PaymentState state;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "paypal_order_id")
    private String paypalOrderId;

    @Column(name = "paypal_capture_id")
    private String paypalCaptureId;

    public Payment() {}

    public Payment(CreatePaymentCommand command) {
        this.amount = command.amount();
        this.patientId = command.patientId();
        this.state = PaymentState.PENDING;
        this.appointmentId = command.appointmentId();
        this.paymentDate = null;
        this.paypalOrderId = null;
        this.paypalCaptureId = null;
    }
}
