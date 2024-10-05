package com.graduates.test.service.impl;

import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserResposity userRepository;

//    public Page<UserEntity> getFilteredUsers(String username, String email, String fullname, String dob, String phone, String address, int page,int sizes) {
//        // Tạo một Predicate hoặc Specification tùy thuộc vào yêu cầu của bạn
//        Pageable pageable = PageRequest.of(page, sizes);
//        return userRepository.findAllByFilters(username, email, fullname, dob, phone, address, pageable);
//    }

    @Override
    public Page<UserEntity> searchUser(String username, String email, String fullname, String dob, String phone, String address, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
        return userRepository.searchUser(username, email, fullname, dob, phone,  pageable);
    }

//    public Page<Book> getList(String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer quantity, Integer price, String category, String publisher, String distributor, int page, int sizes) {
//        Pageable pageable = PageRequest.of(page, sizes);
//        return bookCategoryResposity.searchBooks(nameBook, author, category, publisher, distributor,pageable);
//    }
}
