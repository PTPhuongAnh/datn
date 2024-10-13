package com.graduates.test.service.impl;

import com.graduates.test.model.Feedback;
import com.graduates.test.model.Order;
import com.graduates.test.model.OrderDetail;
import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.FeedbackRepository;
import com.graduates.test.resposity.OrderDetailRepository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public String addFeedback(Integer userId, Integer orderDetailId, String comment, Integer rating) {
        UserEntity user = userResposity.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm chi tiết đơn hàng dựa trên orderDetailId
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Order Detail not found"));
        Order order = orderDetail.getOrder();  // orderDetail chứa thông tin về Order
        if (order.getOrderStatus().getIdStatus() != 4) {  // Kiểm tra trạng thái của đơn hàng (id_status = 5 -> DELIVERY)
            return "Order status is not 'DELIVERY'. Feedback cannot be added.";
        }

        // Kiểm tra xem người dùng có phải là chủ của đơn hàng không
//        if (!order.getUser().getId().equals(userId)) {
//            return "User does not own this order detail.";
//        }

        // Kiểm tra xem chi tiết đơn hàng có thuộc về đơn hàng của người dùng hay không
        if (!orderDetail.getOrder().getUser().getIdUser().equals(userId)) {
            return "User does not own this order detail.";
        }

        // Tạo đối tượng Feedback
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setOrderDetail(orderDetail);
        feedback.setComment(comment);
        feedback.setRating(rating);
        feedback.setCreatedAt(LocalDateTime.now());

        // Lưu phản hồi vào cơ sở dữ liệu
        feedbackRepository.save(feedback);

        return "Feedback added successfully";
    }

}

