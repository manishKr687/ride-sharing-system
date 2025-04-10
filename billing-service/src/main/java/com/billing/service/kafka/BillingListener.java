package com.billing.service.kafka;

import com.billing.service.entity.Invoice;
import com.billing.service.repository.InvoiceRepository;
import com.common.model.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BillingListener {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @KafkaListener(topics = "payment_event", groupId = "billing-group", containerFactory = "containerFactory")
    public void consume(PaymentEvent paymentEvent) {
        if ("SUCCESS".equals(paymentEvent.getStatus())) {
            Invoice invoice = new Invoice();
            invoice.setPaymentId(paymentEvent.getPaymentId());
            invoice.setRideId(paymentEvent.getRideId());
            invoice.setUserId(paymentEvent.getUserId());
            invoice.setAmount(paymentEvent.getAmount());
            invoice.setStatus("GENERATED");
            invoice.setCreatedAt(LocalDateTime.now());

            invoiceRepository.save(invoice);
            System.out.println("Invoice generated for Payment: " + paymentEvent.getPaymentId());
        }
        else{
            System.out.println("Invoice not generated for Payment: ");
        }
    }
}
