package com.graduates.test.controller;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.dto.FeedbackResponse;
import com.graduates.test.dto.VoucherRespone;
import com.graduates.test.model.Feedback;
import com.graduates.test.model.Voucher;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
             return ResponseHandler.responeBuilder1(HttpStatus.OK,false,e.getMessage());
         }
     }


    @PutMapping("/update")
    public ResponseEntity<?> updateFeedbacks(
            @RequestHeader("Authorization") String token,
            @RequestBody List<FeedbackRespone> feedbackUpdates) throws Exception {

        token = token.replace("Bearer ", "");
        String result = feedbackService.updateFeedbacks(token, feedbackUpdates);
        if (result.equals("All feedbacks updated successfully")) {
            return ResponseHandler.responeBuilder1(HttpStatus.OK,true,result);
        } else {
            return ResponseHandler.responeBuilder1(HttpStatus.OK,false,result);
        }
    }

//    @GetMapping("/list")
//    public ResponseEntity<?> getAllFeedbacks(
//
//    ) {
//        List<FeedbackResponse> feedbackResponses = feedbackService.getAllFeedbacks();
//        return ResponseHandler.responeBuilder(HttpStatus.OK,true,feedbackResponses);
//    }
    @GetMapping("/list")
    public ResponseEntity<?> getAllFeedbacks(
            @RequestParam(value = "nameBook", required = false) String nameBook,
            @RequestParam(value = "username", required = false) String account,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Feedback> publishersPage = feedbackService.getList(nameBook,account,page, size);
        if (publishersPage.isEmpty()) {
            return ResponseHandler.responeBuilder( HttpStatus.OK, false, "No feedback found");
        } else {
            List<FeedbackResponse> publisherRespones = publishersPage.getContent().stream()
                    .map(this::convertToFeedbackResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("data", publisherRespones);
            response.put("currentPage", publishersPage.getNumber());
            response.put("totalItems", publishersPage.getTotalElements());
            response.put("totalPages", publishersPage.getTotalPages());

            return ResponseHandler.responeBuilder( HttpStatus.OK, true, response);
        }}

//        List<FeedbackResponse> feedbackResponses = feedbackService.getAllFeedbacks();
//        return ResponseHandler.responeBuilder(HttpStatus.OK,true,feedbackResponses);

    private FeedbackResponse convertToFeedbackResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(feedback.getIdFeedback());
        response.setUsername(feedback.getUser().getUsername());
        response.setBookTitle(feedback.getOrderDetail().getBook().getNameBook());
        response.setComment(feedback.getComment());
        response.setRating(feedback.getRating());
        response.setCreatedAt(feedback.getCreatedAt());
        if(feedback.getVisible()==null){
            response.setVisible(false);
        }else {
            response.setVisible(feedback.getVisible());
        }
        return response;
    }



    @PostMapping("disable/{id}")
    public ResponseEntity<?> toggleFeedbackVisibility(@PathVariable Integer id) {
        Feedback updatedFeedback = feedbackService.toggleVisibility(id);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,updatedFeedback) ;
    }
}
