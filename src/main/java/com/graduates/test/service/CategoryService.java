package com.graduates.test.service;

import com.graduates.test.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    public String createCategory(Category category);
//    public String updateCategory(int idCategory, Category category);

    public String updateCategory(int idCategory, String category, MultipartFile file);

    public String deleteCategory(Integer idCategory);
    public Category getCategory(Integer idCategory);
    public List<Category> getAllCategory();

    String saveImage(MultipartFile file);
    String getUploadDir();

    Page<Category> getAllCategories(int page, int size);
}
