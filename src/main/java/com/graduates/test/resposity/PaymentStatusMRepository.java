package com.graduates.test.resposity;

import com.graduates.test.model.PaymentStatusM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusMRepository extends JpaRepository<PaymentStatusM,Integer> {
    PaymentStatusM findByStatusName(String statusName);
}
