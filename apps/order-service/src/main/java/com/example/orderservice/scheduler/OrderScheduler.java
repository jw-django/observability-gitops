package com.example.orderservice.scheduler;

import com.example.orderservice.client.PaymentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final PaymentClient paymentClient;

    // 3초마다 자동 실행으로 더미 트래픽 생성
    @Scheduled(fixedRate = 3000)
    public void createOrderAuto() {
        log.info(">>> [자동 주문] 새로운 주문 생성을 시작합니다.");

        try {
            String result = paymentClient.processPayment();
            log.info("<<< [자동 주문] 결제 완료. 결과: {}", result);
        } catch (Exception e) {
            log.error("ERROR!!! [자동 주문] 결제 실패: {}", e.getMessage());
        }
    }
}
