package com.graduates.test.service;

import com.graduates.test.model.Publisher;
import com.graduates.test.resposity.PublisherResposity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PublisherService {
    public String createPublisher(Publisher publisher);
    public String updatePublisher(Publisher publisher);
    public String deletePublisher(Integer idPublisher);
    public Publisher getPublisher(Integer idPublisher);
    public List<Publisher> getAllPublisher();

    Publisher findById(Integer idPublisher);

    Page<Publisher> getList(String namePublisher, String addressPublisher, int page, int size);

    void markPublisherAsDeleted(Integer idPublisher);
}
