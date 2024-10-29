package com.graduates.test.service.impl;

import com.graduates.test.dto.FeedbackRespone;
import com.graduates.test.model.*;
import com.graduates.test.resposity.FeedbackRepository;
import com.graduates.test.resposity.OrderDetailRepository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserResposity userResposity, OrderDetailRepository orderDetailRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userResposity = userResposity;
        this.orderDetailRepository = orderDetailRepository;
    }
    @Override
    public String addFeedbacks(Integer userId, Integer orderId, List<FeedbackRespone> feedbackDTOs) {
        UserEntity user = userResposity.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

            feedbackRepository.save(feedback);
        }

        return "Feedbacks added successfully";
    }



//    public String addFeedbacks(Integer userId, List<FeedbackRespone> feedbackDTOs) {
//        UserEntity user = userResposity.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        for (FeedbackRespone feedbackDTO : feedbackDTOs) {
//            OrderDetail orderDetail = orderDetailRepository.findById(feedbackDTO.getOrderDetailId())
//                    .orElseThrow(() -> new RuntimeException("Order Detail not found"));
//
//            Order order = orderDetail.getOrder();
//            if (order.getOrderStatus().getIdStatus() != 4) {
//                return "Order status is not 'DELIVERY'. Feedback cannot be added.";
//            }
//
//            if (!order.getUser().getIdUser().equals(userId)) {
//                return "User does not own this order detail.";
//            }
//
//            Book book = orderDetail.getBook();
//            if (book == null || !book.getIdBook().equals(feedbackDTO.getBookId())) {
//                return "The book does not belong to this order detail.";
//            }
//
//            Feedback feedback = new Feedback();
//            feedback.setUser(user);
//            feedback.setOrderDetail(orderDetail);
//            feedback.setComment(feedbackDTO.getComment());
//            feedback.setRating(feedbackDTO.getRating());
//            feedback.setCreatedAt(LocalDateTime.now());
//
//            feedbackRepository.save(feedback);
//        }
//
//        return "Feedbacks added successfully";
//    }


    public String updateFeedbacks(Integer userId, List<FeedbackRespone> feedbackUpdates) {
        UserEntity user = userResposity.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
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

            // Lưu thay đổi
            feedbackRepository.save(feedback);
        }
        return "All feedbacks updated successfully";
    }

}

