package com.graduates.test.controller;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.PublisherRespone;
import com.graduates.test.dto.VoucherRespone;
import com.graduates.test.model.Publisher;
import com.graduates.test.model.Voucher;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    private final VoucherService voucherService;
    @Autowired
    private JwtService jwtService;

    public VoucherController(VoucherService voucherService, JwtService jwtService) {
        this.voucherService = voucherService;
        this.jwtService = jwtService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getVoucherList(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        System.out.println("Voucher API called with params - Code: " + code + ", Page: " + page + ", Size: " + size);
        Page<Voucher> publishersPage = voucherService.getList(code,page, size);

        if (publishersPage.isEmpty()) {
            return ResponseHandler.responeBuilder( HttpStatus.OK, false, "No voucher found");
        } else {
            List<VoucherRespone> publisherRespones = publishersPage.getContent().stream()
                    .map(this::convertVoucherResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("voucher", publisherRespones);
            response.put("currentPage", publishersPage.getNumber());
            response.put("totalItems", publishersPage.getTotalElements());
            response.put("totalPages", publishersPage.getTotalPages());

            return ResponseHandler.responeBuilder( HttpStatus.OK, true, response);
        }}
    private VoucherRespone convertVoucherResponse(Voucher publisher) {

        VoucherRespone response = new VoucherRespone();
        response.setId(publisher.getId());
        response.setCode(publisher.getCode());
        response.setMaxU(publisher.getMaxUsage());
        response.setMinO(publisher.getMinOrderValue());
        response.setCreateAt(publisher.getCreateAt());
        response.setUpdateAt(publisher.getUpdateAt());
        response.setCreated_by(publisher.getCreatedBy());
        response.setUpdated_by(publisher.getUpdatedBy());
        response.setDiscount(publisher.getDiscountValue());
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        return voucherService.getVoucherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVoucher(
            @RequestParam("code") String code, // ten
            @RequestParam("discountValue") Double discountValue,
            @RequestParam("maxUsage") Integer maxUsage,
            @RequestParam("minOrderValue") Double minO,
            @RequestParam("startDate")LocalDateTime start,
            @RequestParam("endDate") LocalDateTime end,
            @RequestHeader("Authorization") String token
            ) {

        try {
            token = token.replace("Bearer ", "");
            String username = jwtService.extractUsername(token);
            System.out.println("username "+ username);
            // Tạo đối tượng Publisher
          Voucher publisher = new Voucher();
            publisher.setCode(code);
            publisher.setDiscountValue(discountValue);
            publisher.setMaxUsage(maxUsage);
            publisher.setMinOrderValue(minO);
            publisher.setStartDate(start);
            publisher.setEndDate(end);
            publisher.setCreatedBy(username);
            publisher.setUpdatedBy(username);
            // Tạo nhà xuất bản mới
            String result = voucherService.createVoucher(publisher);

            // Trả về phản hồi thành công với thông điệp từ service
            return ResponseHandler.responeBuilder(HttpStatus.OK, true, result);

        } catch (Exception e) {
            // Xử lý các lỗi không dự đoán trước
            e.printStackTrace();  // In chi tiết lỗi ra console
            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, "An error occurred while creating the voucher: " + e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateVoucher(
            @PathVariable(value = "id", required = false) Integer idVoucher,
            @RequestParam(value = "maxUsage",required = false) Integer maxUsage,
            @RequestParam(value = "ate",required = false) Double minO,
            @RequestParam(value = "startDate",required = false)LocalDateTime start,
            @RequestParam(value = "endDate",required = false) LocalDateTime end,
            @RequestHeader("Authorization") String token
            ) {
        try {
            token = token.replace("Bearer ", "");
            String username = jwtService.extractUsername(token);
            // Tìm kiếm và kiểm tra nhà xuất bản hiện tại
          Voucher existingPublisher = voucherService.findById(idVoucher);
            if (existingPublisher == null) {
                // Nhà xuất bản không tồn tại
                return ResponseHandler.responeBuilder( HttpStatus.NOT_FOUND, false, "Publisher not found");
            }

            // Cập nhật các thông tin từ các tham số được cung cấp
            if (maxUsage != null) existingPublisher.setMaxUsage(maxUsage);
            if (minO != null) existingPublisher.setMinOrderValue(minO);
            if(start!=null) existingPublisher.setStartDate(start);
            if(end!=null) existingPublisher.setEndDate(end);
            existingPublisher.setUpdatedBy(username);

            // Gọi service để cập nhật nhà xuất bản
            String result = voucherService.updateVoucher(existingPublisher);

            // Trả về phản hồi thành công
            return ResponseHandler.responeBuilder( HttpStatus.OK, true, result);

        } catch (Exception e) {
            // Xử lý lỗi và trả về mã lỗi 500
            return ResponseHandler.responeBuilder( HttpStatus.INTERNAL_SERVER_ERROR, false, "Error updating publisher");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/disable/{voucherId}")
    public ResponseEntity<?> updateVoucherStatus(@PathVariable Integer voucherId) {
        try {
            voucherService.updateVoucherStatus(voucherId);  // Cập nhật trạng thái của voucher
//            return isActive ? "Voucher enabled successfully." : "Voucher disabled successfully.";

                return ResponseHandler.responeBuilder(HttpStatus.OK,true,"Thay đổi trạng thái thành công");

        } catch (Exception e) {
            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR,false,e.getMessage());
        }
    }
}
