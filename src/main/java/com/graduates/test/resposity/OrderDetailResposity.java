package com.graduates.test.resposity;

import com.graduates.test.model.OrderDetail;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailResposity extends JpaRepository<OrderDetail,Integer> {
}
