package com.graduates.test.resposity;

import com.graduates.test.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentResponsitory extends JpaRepository<Payment,Integer> {

}
