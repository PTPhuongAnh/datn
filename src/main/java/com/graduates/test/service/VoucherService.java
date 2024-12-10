package com.graduates.test.service;

import com.graduates.test.model.Voucher;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface VoucherService {
    List<Voucher> getAllVouchers();
    Optional<Voucher> getVoucherById(Integer id);
   String createVoucher(Voucher voucher);



    String updateVoucher(Voucher updatedVoucher);





    void deleteVoucher(Integer id);
    Voucher findById(Integer idVoucher);

    Page<Voucher> getList(String code, int page, int size);

    void updateVoucherStatus(Integer voucherId);
}
