package com.graduates.test.resposity;

import com.graduates.test.model.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherResposity extends JpaRepository<Publisher,Integer> {
    Page<Publisher> findAllByDeletedFalse(Pageable pageable);

    Page<Publisher> findByNamePublisherContainingOrAddressPublisherContaining(String namePublisher, String addressPublisher, Pageable pageable);
}
