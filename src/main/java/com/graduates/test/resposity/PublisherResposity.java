package com.graduates.test.resposity;

import com.graduates.test.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherResposity extends JpaRepository<Publisher,Integer> {
}
