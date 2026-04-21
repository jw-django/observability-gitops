package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-client", url = "http://payment-servce:8080")
public interface PaymentClient {

    @PostMapping("/api/payments")
    String processPayment();
}
