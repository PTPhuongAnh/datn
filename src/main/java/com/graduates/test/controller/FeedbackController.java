package com.graduates.test.controller;

import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<?> addFeedback(
            @RequestParam Integer userId,
            @RequestParam Integer orderDetailId,
            @RequestParam String comment,
            @RequestParam Integer rating) {

        String result = feedbackService.addFeedback(userId, orderDetailId, comment, rating);

        // Kiểm tra kết quả trả về từ service và xử lý tương ứng
        if (result.equals("User does not own this order detail.")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        } else if (result.equals("Order status is not 'DELIVERY'. Feedback cannot be added.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseHandler.responeBuilder(HttpStatus.OK, true, result);
    }
}
