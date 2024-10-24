package com.graduates.test.resposity;

import com.graduates.test.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    boolean existsByBook_IdBook(Integer idBook);

   // List<Object[]> findBooksByTotalSoldWithDeliveredStatus();
    @Query("SELECT od.book.idBook, SUM(od.quantity) as totalSold " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +  // Tham gia với bảng Order
            "WHERE o.orderStatus.status = 'Completed' " +  // Điều kiện chỉ lấy các đơn hàng có trạng thái giao thành công
            "GROUP BY od.book.idBook " +  // Nhóm theo sách để tính tổng số lượng bán
            "ORDER BY totalSold DESC")  // Sắp xếp theo số lượng bán, từ cao đến thấp
    List<Object[]> findBooksByTotalSoldWithDeliveredStatus();
}
