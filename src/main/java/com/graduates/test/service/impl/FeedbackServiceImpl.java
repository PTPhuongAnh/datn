package com.graduates.test.service.impl;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.dto.FeedbackResponse;
import com.graduates.test.model.*;
import com.graduates.test.resposity.FeedbackRepository;
import com.graduates.test.resposity.OrderDetailRepository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.FeedbackService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserResposity userResposity;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private JwtService jwtService;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserResposity userResposity, OrderDetailRepository orderDetailRepository, JwtService jwtService) {
        this.feedbackRepository = feedbackRepository;
        this.userResposity = userResposity;
        this.orderDetailRepository = orderDetailRepository;
        this.jwtService = jwtService;
    }

    @Override
    public String addFeedbacks(String token, Integer orderId, List<FeedbackRespone> feedbackDTOs) throws Exception {
        String username = jwtService.extractUsername(token);  // Lấy username từ token

        // Tìm userId dựa trên username
        UserEntity user = userResposity.findByUsername(username)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng với username: " + username));
        Integer userId = user.getIdUser();  // Lấy userId từ đối tượng UserEntity
//        UserEntity user = userResposity.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        for (FeedbackRespone feedbackDTO : feedbackDTOs) {
            OrderDetail orderDetail = orderDetailRepository.findById(feedbackDTO.getOrderDetailId())
                    .orElseThrow(() -> new RuntimeException("Order Detail not found"));

            // Kiểm tra xem orderDetail có thuộc về orderId hay không
            Order order = orderDetail.getOrder();
            if (!order.getId().equals(orderId)) {
                return "Order detail does not belong to the given order.";
            }

            if (order.getOrderStatus().getIdStatus() != 4) {
                return "Order status is not 'DELIVERY'. Feedback cannot be added.";
            }

            if (!order.getUser().getIdUser().equals(userId)) {
                return "User does not own this order detail.";
            }

            Book book = orderDetail.getBook();
            if (book == null || !book.getIdBook().equals(feedbackDTO.getBookId())) {
                return "The book does not belong to this order detail.";
            }

            Feedback feedback = new Feedback();
            feedback.setUser(user);
            feedback.setOrderDetail(orderDetail);
            feedback.setComment(feedbackDTO.getComment());
            feedback.setRating(feedbackDTO.getRating());
            feedback.setCreatedAt(LocalDateTime.now());
            feedback.setCreatedBy(username);
            feedback.setUpdatedBy(username);
            feedback.setIsVisible(false);

            feedbackRepository.save(feedback);
        }

        return "Feedbacks added successfully";
    }





    public String updateFeedbacks(String token, List<FeedbackRespone> feedbackUpdates) throws Exception {
        String username = jwtService.extractUsername(token);  // Lấy username từ token

        // Tìm userId dựa trên username
        UserEntity user = userResposity.findByUsername(username)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng với username: " + username));
        Integer userId = user.getIdUser();  // Lấy userId từ đối tượng UserEntity
//        UserEntity user = userResposity.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
        for (FeedbackRespone feedbackUpdate : feedbackUpdates) {
            // Tìm Feedback theo feedbackId
            Feedback feedback = feedbackRepository.findById(feedbackUpdate.getIdFeedback())
                    .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackUpdate.getIdFeedback()));

            // Kiểm tra người dùng có quyền cập nhật feedback hay không
            if (!feedback.getUser().getIdUser().equals(userId)) {
                return "User does not have permission to update feedback with ID: " + feedbackUpdate.getIdFeedback();
            }

            // Cập nhật các thông tin
            feedback.setComment(feedbackUpdate.getComment());
            feedback.setRating(feedbackUpdate.getRating());
            feedback.setUpdateAt(LocalDateTime.now());
            feedback.setUpdatedBy(username);

            // Lưu thay đổi
            feedbackRepository.save(feedback);
        }
        return "All feedbacks updated successfully";
    }

    public List<FeedbackResponse> getAllFeedbacks() {
//        List<Feedback> feedbacks = feedbackRepository.findAllByOrderByCreatedAtDesc();
//        return feedbacks.stream()
//                .filter(Feedback::getIsVisible)  // Chỉ trả về những feedback có trạng thái là hiển thị
//                .map(this::convertToFeedbackResponse)
//                .collect(Collectors.toList());
      //  return feedbackRepository.findAll();
    //    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return feedbackRepository.findAll()  // Lấy toàn bộ danh sách Feedback
                .stream()  // Chuyển đổi thành stream
                .map(this::convertToFeedbackResponse)  // Chuyển từng Feedback thành FeedbackResponse
                .collect(Collectors.toList());  // Thu thập vào một danh sách
    }



    public Feedback toggleVisibility(Integer idFeedback) {
        Feedback feedback = feedbackRepository.findById(idFeedback)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));
        feedback.setIsVisible(!feedback.getIsVisible());
        return feedbackRepository.save(feedback);
    }

    @Override
    public Page<Feedback> getList(String name, String account, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (account!= null && !account.isEmpty()) {
            return feedbackRepository.findByUserUsernameContaining(account, pageable);
        } else {
            return feedbackRepository.findAll(pageable);
        }

    }


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




}

