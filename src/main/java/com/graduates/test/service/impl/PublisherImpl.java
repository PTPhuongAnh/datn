package com.graduates.test.service.impl;

import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.model.Distributor;
import com.graduates.test.model.Publisher;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.PublisherResposity;
import com.graduates.test.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PublisherImpl implements PublisherService {
    private PublisherResposity publisherResposity;
    private BookCategoryResposity bookCategoryResposity;

    public PublisherImpl(PublisherResposity publisherResposity, BookCategoryResposity bookCategoryResposity) {
        this.publisherResposity = publisherResposity;
        this.bookCategoryResposity = bookCategoryResposity;
    }

    @Override
    public String createPublisher(Publisher publisher) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Kiểm tra nếu người dùng không có vai trò "ROLE_ADMIN", trả về lỗi
//        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            return "Unauthorized: Only admin can create a publisher";
//        }

        // Kiểm tra và tạo mã publisher
        if (publisher.getPublisherCode() == null || publisher.getPublisherCode().isEmpty()) {
            publisher.setPublisherCode(generatePublisherCode());
        }

        if (publisherResposity.existsByPublisherCode(publisher.getPublisherCode())) {
            return "Publisher code already exists!";
        }

        publisherResposity.save(publisher);
        return "Create publisher success";
    }
//        if(publisher.getPublisherCode()== null || publisher.getPublisherCode().isEmpty()){
//            publisher.setPublisherCode(generatePublisherCode());
//        }
//        if (publisherResposity.existsByPublisherCode(publisher.getPublisherCode())) {
//            return "Publisher code already exists!";
//        }
//        publisherResposity.save(publisher);
//        return "Create publisher success";

    private String generatePublisherCode() {
        // Bạn có thể thay đổi cách sinh mã này để phù hợp với yêu cầu của bạn
        return  UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    public Publisher findById(Integer idPublisher) {
        // Tìm kiếm nhà xuất bản theo id
        return publisherResposity.findById(idPublisher)
                .orElse(null); // Trả về null nếu không tìm thấy
    }

    public String updatePublisher(Publisher publisher) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Kiểm tra nếu người dùng không có vai trò "ROLE_ADMIN", trả về lỗi
//        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            return "Unauthorized: Only admin can update a publisher";
//        }
        // Thực hiện logic cập nhật nhà xuất bản trong cơ sở dữ liệu
//      publisherResposity.save(publisher);
//        return "Publisher updated successfully.";
        publisherResposity.save(publisher);
        return "Publisher updated successfully.";
    }


    public boolean isPublisherUsed(Integer idPublisher) {
        return bookCategoryResposity.existsByPublisher_IdPublisher(idPublisher);
    }



    public void markPublisherAsDeleted(Integer idPublisher) {
        if (isPublisherUsed(idPublisher)) {
            throw new IllegalStateException("Cannot delete publisher. It is used in one or more books.");
        }

        Optional<Publisher> publisherOptional = publisherResposity.findById(idPublisher);
        if (!publisherOptional.isPresent()) {
            throw new IllegalStateException("publisher not found.");
        }

        Publisher publisher = publisherOptional.get();
        publisher.setDeleted(true); // Đánh dấu là đã bị xóa
       publisherResposity.save(publisher); // Lưu thay đổi
    }

    public Page<Publisher> getList(String namePublisher, String addressPublisher, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
//        return categoryResposity.searchCategory(nameCategory,pageable);
        if (namePublisher != null && !namePublisher.isEmpty()||addressPublisher!=null&&addressPublisher.isEmpty()) {
            return publisherResposity.findByNamePublisherContainingOrAddressPublisherContaining(namePublisher,addressPublisher, pageable);
        } else {
            return publisherResposity.findAllByDeletedFalse(pageable);
        }
    }
    @Override
    public String deletePublisher(Integer idPublisher) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Kiểm tra nếu người dùng không có vai trò "ROLE_ADMIN", trả về lỗi
//        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            throw new IllegalStateException("Unauthorized: Only admin can delete a publisher");
//        }
        if (isPublisherUsed(idPublisher)) {
            throw new IllegalStateException("Cannot delete category. It is used in one or more books.");
        }
        publisherResposity.deleteById(idPublisher);
        return "Category deleted successfully.";
    }


    @Override
    public Publisher getPublisher(Integer idPublisher) {
      return  publisherResposity.findById(idPublisher)
              .orElseThrow(() -> new ResourceNotFoundException("Publisher with ID " + idPublisher + " not found"));

    }

    @Override
    public List<Publisher> getAllPublisher() {
        return publisherResposity.findAll();
    }
}
