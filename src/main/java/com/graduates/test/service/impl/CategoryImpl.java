package com.graduates.test.service.impl;

import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Book;
import com.graduates.test.model.Category;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.CategoryResposity;
import com.graduates.test.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CategoryImpl implements CategoryService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public CategoryImpl(CategoryResposity categoryResposity, BookCategoryResposity bookCategoryResposity) {
        this.categoryResposity = categoryResposity;
        this.bookCategoryResposity = bookCategoryResposity;
    }

    private CategoryResposity categoryResposity;

    private BookCategoryResposity bookCategoryResposity;




    public String getUploadDir() {
//        return uploadDir.replace("\\", "/");
        return uploadDir;
    }

    @Override
    public String createCategory(Category category) {
        categoryResposity.save(category);
        return "Create category success";
    }



    @Override
    public String updateCategory(int idCategory, String category, MultipartFile file) {
        Optional<Category> existingCategory = categoryResposity.findById(idCategory);
        if (existingCategory.isPresent()) {
            Category updatedCategory = existingCategory.get();
            updatedCategory.setNameCategory(category);

            if (file != null && !file.isEmpty()) {
                String fileName = saveImage(file);
                updatedCategory.setImage(fileName);
            }

            categoryResposity.save(updatedCategory);
            return "Update category success";
        } else {
            return "Category not found";
        }
    }




    @Override
    public Category getCategory(Integer idCategory) {
        return categoryResposity.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with ID " + idCategory + " not found"));

    }

    @Override
    public List<Category> getAllCategory() {
        return categoryResposity.findAll();
    }

    public Page<Category> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryResposity.findAll(pageable);
    }

    @Override
    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try {
            // Xác định đường dẫn lưu trữ
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // Tạo tên tệp duy nhất để tránh xung đột
            String originalFileName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFileName;  // Sử dụng timestamp để tạo tên file duy nhất
            Path filePath = path.resolve(fileName);

            // Sao chép tệp vào thư mục đích
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Ghi thông báo thành công
            System.out.println("Saved file: " + filePath.toString());

            return fileName;

        } catch (FileAlreadyExistsException e) {
            throw new RuntimeException("File already exists: " + e.getMessage(), e);
        } catch (IOException e) {
            // Ném lỗi nếu có vấn đề trong quá trình lưu trữ
            throw new RuntimeException("Failed to store image file", e);
        }
    }

    public List<Category> getActiveCategories() {
        return categoryResposity.findByDeletedFalse(); // Lấy danh sách category chưa bị xóa
    }


    //kiểm tra idcate co tồn tại book
    public boolean isCategoryUsed(Integer idCategory) {
        return bookCategoryResposity.existsByCategory_IdCategory(idCategory);
    }

    @Override
    public String deleteCategory(Integer idCategory) {
        return null;
    }
    public void markCategoryAsDeleted(Integer idCategory) {
        if (isCategoryUsed(idCategory)) {
            throw new IllegalStateException("Cannot delete category. It is used in one or more books.");
        }

        Optional<Category> categoryOptional = categoryResposity.findById(idCategory);
        if (!categoryOptional.isPresent()) {
            throw new IllegalStateException("Category not found.");
        }

        Category category = categoryOptional.get();
        category.setDeleted(true); // Đánh dấu là đã bị xóa
        categoryResposity.save(category); // Lưu thay đổi
    }
    public Page<Category> getList(String nameCategory, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
//        return categoryResposity.searchCategory(nameCategory,pageable);
        if (nameCategory != null && !nameCategory.isEmpty()) {
            return categoryResposity.findByNameCategoryContaining(nameCategory, pageable);
        } else {
            return categoryResposity.findAllByDeletedFalse(pageable);
        }
    }
}
