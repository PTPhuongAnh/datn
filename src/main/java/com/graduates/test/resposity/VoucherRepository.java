package com.graduates.test.resposity;

import com.graduates.test.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher,Integer> {


    Page<Voucher> findByCodeContaining(String code, Pageable pageable);
}
