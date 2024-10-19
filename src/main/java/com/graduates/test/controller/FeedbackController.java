package com.graduates.test.controller;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.model.Feedback;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<?> addFeedback(
            @RequestParam Integer userId,
            @RequestParam Integer orderDetailId,
            @RequestParam Integer bookId,
            @RequestParam String comment,
            @RequestParam Integer rating) {

        String result = feedbackService.addFeedback(userId, orderDetailId,bookId, comment, rating);

        // Kiểm tra kết quả trả về từ service và xử lý tương ứng
        if (result.equals("User does not own this order detail.")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        } else if (result.equals("Order status is not 'DELIVERY'. Feedback cannot be added.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseHandler.responeBuilder(HttpStatus.OK, true, result);
    }
    @GetMapping("/list")
    public ResponseEntity<?> getFeedbackByBookId(@RequestParam Integer bookId) {
        List<FeedbackRespone> feedbacks = feedbackService.getFeedbackResponsesByBookId(bookId);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,feedbacks);
    }
}
