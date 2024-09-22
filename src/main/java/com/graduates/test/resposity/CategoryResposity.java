package com.graduates.test.resposity;

import com.graduates.test.model.Book;
import com.graduates.test.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryResposity extends JpaRepository <Category,Integer>{

}
