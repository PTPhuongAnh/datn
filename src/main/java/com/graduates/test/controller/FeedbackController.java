package com.graduates.test.controller;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.dto.FeedbackResponse;
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
     public ResponseEntity<?> addFeedbacks(
             @RequestHeader("Authorization") String token,
             @RequestParam("orderId") Integer orderId,
             @RequestBody List<FeedbackRespone> feedbackDTOs) {
         try {
             token = token.replace("Bearer ", "");
             String response = feedbackService.addFeedbacks(token, orderId, feedbackDTOs);
             return ResponseHandler.responeBuilder(HttpStatus.OK,true,response);
         } catch (Exception e) {
             return ResponseHandler.responeBuilder(HttpStatus.OK,false,e.getMessage());
         }
     }


    @PutMapping("/update")
    public ResponseEntity<?> updateFeedbacks(
            @RequestHeader("Authorization") String token,
            @RequestBody List<FeedbackRespone> feedbackUpdates) throws Exception {

        token = token.replace("Bearer ", "");
        String result = feedbackService.updateFeedbacks(token, feedbackUpdates);
        if (result.equals("All feedbacks updated successfully")) {
            return ResponseHandler.responeBuilder(HttpStatus.OK,true,result);
        } else {
            return ResponseHandler.responeBuilder(HttpStatus.OK,false,result);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllFeedbacks() {
        List<FeedbackResponse> feedbackResponses = feedbackService.getAllFeedbacks();
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,feedbackResponses);
    }

    // Thay đổi trạng thái hiển thị của feedback (Ẩn hoặc Hiện)
    @PutMapping("/{id}/visibility")
    public ResponseEntity<?> changeFeedbackVisibility(@PathVariable Integer id, @RequestParam Boolean isVisible) {
        feedbackService.changeVisibility(id, isVisible);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,"success");
    }
}
