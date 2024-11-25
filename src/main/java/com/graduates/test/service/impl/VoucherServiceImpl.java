package com.graduates.test.service.impl;

import com.graduates.test.Config.JwtService;
import com.graduates.test.model.UserEntity;
import com.graduates.test.model.Voucher;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.resposity.VoucherRepository;
import com.graduates.test.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Autowired
    private UserResposity userRepository;

    @Autowired
    private JwtService jwtService; // Giả sử bạn có JwtService để giải mã token

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    @Override
    public String createVoucher(Voucher voucher) {
        // Lấy thông tin người dùng từ token

        // Lưu voucher vào cơ sở dữ liệu
        voucherRepository.save(voucher);

        return "Voucher created successfully";
    }

    @Override
    public String updateVoucher(Voucher updatedVoucher) {
            voucherRepository.save(updatedVoucher);
            return "Voucher updated successfully";

    }

    @Override
    public void deleteVoucher(Integer id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Voucher findById(Integer idVoucher) {

            // Tìm kiếm nhà xuất bản theo id
            return voucherRepository.findById(idVoucher)
                    .orElse(null); // Trả về null nếu không tìm thấy
        }

    @Override
    public Page<Voucher> getList(String code, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (code != null && !code.isEmpty()) {
            return voucherRepository.findByCodeContaining(code, pageable);
        } else {
            return voucherRepository.findAll(pageable);
        }

    }
}


