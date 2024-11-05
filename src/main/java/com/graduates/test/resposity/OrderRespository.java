package com.graduates.test.resposity;

import com.graduates.test.dto.CategorySalesDTO;
import com.graduates.test.model.Cart;
import com.graduates.test.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRespository extends JpaRepository<Order,Integer> {
    boolean existsByCart(Cart cart);
    List<Order> findByUser_idUserOrderByCreatedAtDesc(Integer userId);
    Optional<Order> findByIdAndUser_idUser(Integer orderId, Integer userId);
    List<Order> findByUser_IdUserAndOrderStatus_IdStatus(Integer userId, Integer statusId);


    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double calculateTotalRevenue();


    @Query("SELECT new com.graduates.test.dto.CategorySalesDTO(c.nameCategory, SUM(od.quantity)) " +
            "FROM OrderDetail od JOIN od.book.category c " +
            "WHERE od.order.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY c.nameCategory")
    List<CategorySalesDTO> countBooksSoldByCategory(@Param("startDate")LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);



    @Query("SELECT MONTH(o.createdAt) AS month, SUM(o.totalAmount) AS totalRevenue " +
            "FROM Order o WHERE YEAR(o.createdAt) = :currentYear " +
            "GROUP BY MONTH(o.createdAt) " +
            "ORDER BY MONTH(o.createdAt)")
    List<Object[]> getMonthlyRevenue(@Param("currentYear") int currentYear);

    boolean existsByOrderCode(String orderCode);


    @Query(value = """
            SELECT o.*, f.id_feedback , f.user_id, u.username, f.order_detail_id,  b.name_book,
                   f.comment, f.rating, f.created_at, f.update_at
            FROM orders o
            LEFT JOIN feedback f ON o.id_order = f.order_id
            LEFT JOIN book b ON f.book_id = b.id
            LEFT JOIN user u ON f.user_id = u.id_user
            WHERE o.id = :orderId
              AND o.user_id = :userId
            """, nativeQuery = true)
    List<Object[]> findOrderWithFeedbackByUser(@Param("orderId") Integer orderId, @Param("userId") Integer userId);

    //  Optional<Order> findByIdAndUserId(Integer orderId, Integer userId);
}
// em đang bị bug bên be phần get  fb ở detail orde