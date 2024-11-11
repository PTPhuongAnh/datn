package com.graduates.test.model;

import com.graduates.test.resposity.PaymentStatusMRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class DataInitializer {
//    @Autowired
//    private PaymentStatusMRepository paymentStatusRepository;
//
//    @PostConstruct
//    public void init() {
//        List<String> statuses = Arrays.asList("UNPAID", "PAID", "FAILED");
//        for (String status : statuses) {
//            if (!paymentStatusRepository.findByStatusName(status)) {
//                paymentStatusRepository.save(new PaymentStatusM(status));
//            }
//        }
//    }
}
