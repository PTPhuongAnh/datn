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
    public List<FeedbackRespone> getFeedbackResponsesByBookId(Integer bookId) {
        List<Feedback> feedbacks = feedbackRepository.findByOrderDetailBookIdBook(bookId);

        // Chuyển đổi danh sách Feedback sang danh sách FeedbackResponse
        return feedbacks.stream()
                .map(this::convertToFeedbackDTO)
                .collect(Collectors.toList());
    }

    public String addFeedback(Integer userId, Integer orderDetailId, Integer bookId, String comment, Integer rating) {
        // Logic như bạn đã viết trước đó
        UserEntity user = userResposity.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Order Detail not found"));

        Order order = orderDetail.getOrder();
        if (order.getOrderStatus().getIdStatus() != 4) {
            return "Order status is not 'DELIVERY'. Feedback cannot be added.";
        }

        if (!orderDetail.getOrder().getUser().getIdUser().equals(userId)) {
            return "User does not own this order detail.";
        }

        Book book = orderDetail.getBook();
        if (book == null || !book.getIdBook().equals(bookId)) {
            return "The book does not belong to this order detail.";
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setOrderDetail(orderDetail);
        feedback.setComment(comment);
        feedback.setRating(rating);
        feedback.setCreatedAt(LocalDateTime.now());

        feedbackRepository.save(feedback);

        return "Feedback added successfully";
    }


    private FeedbackRespone convertToFeedbackDTO(Feedback feedback) {
        Book book = feedback.getOrderDetail().getBook(); // Lấy thông tin sách từ OrderDetail
        //  List<String> imageUrls = getImageUrlsFromBook(book); // Lấy các đường dẫn ảnh từ sách
        UserEntity user = feedback.getUser(); // Lấy thông tin người dùng
        OrderDetail orderDetail = feedback.getOrderDetail();
        return new FeedbackRespone(
                feedback.getIdFeedback(),
                user.getIdUser(),
                user.getUsername(),
                orderDetail.getIdOrderDetail(),
                book.getIdBook(),
                book.getNameBook(),
                feedback.getComment(),
                feedback.getRating(),
                feedback.getCreatedAt()
        );
    }
}

