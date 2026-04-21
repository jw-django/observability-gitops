package com.example.paymentservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping
    public String processPayment() {
        log.info("결제 요청이 들어왔습니다!");
        try {
            // 약간의 지연시간
            Thread.sleep((long) (Math.random() * 200) + 50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "SUCCESS";
    }
}
