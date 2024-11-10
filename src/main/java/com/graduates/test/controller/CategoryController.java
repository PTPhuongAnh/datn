package com.graduates.test.controller;

import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.dto.CategoryRespone;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{idCategory}")
    public ResponseEntity<Object> getCategoryDetails(@PathVariable("idCategory") int idCategory) {
        Category category = categoryService.getCategory(idCategory);
        try {
            if (category == null) {
                return ResponseHandler.responeBuilder(
                        HttpStatus.NOT_FOUND,
                        false,
                        null
                );
            }
            CategoryRespone responseDTO = convertToCategoryResponse(category);
            return ResponseHandler.responeBuilder(
                    HttpStatus.OK,
                    true,
                    responseDTO
            );
        } catch (Exception e) {
            return ResponseHandler.responeBuilder( HttpStatus.INTERNAL_SERVER_ERROR, false, null);

        }

    }

    //thêm category
    @PostMapping("/create")
    public ResponseEntity<?> createCategoryDetails(
            @RequestParam(value = "nameCategory", required = true) String nameCategory,
            @RequestParam(value = "image", required = true) MultipartFile file
    ) {

        try {
            if (nameCategory == null || nameCategory.trim().isEmpty()) {
                return ResponseHandler.responeBuilder( HttpStatus.OK
                        , false, "Tên category là bắt buộc");
            }

            // Kiểm tra xem file có bị thiếu hay không
            if (file == null || file.isEmpty()) {
                return ResponseHandler.responeBuilder( HttpStatus.OK, false, null);
            }

            // Lưu hình ảnh và lấy đường dẫn hình ảnh
            String imagePath = categoryService.saveImage(file);

            // Tạo đối tượng Category
            Category category = new Category();
            category.setNameCategory(nameCategory);
            category.setImage(imagePath);

            // Tạo danh mục mới
            String result = categoryService.createCategory(category);

            // Trả về phản hồi thành công với thông điệp từ service
            return ResponseHandler.responeBuilder( HttpStatus.OK, true, result);

        } catch (Exception e) {
            // Xử lý các ngoại lệ khác nếu có
            System.out.println("Error creating category: " + e.getMessage());
            e.printStackTrace();
            return ResponseHandler.responeBuilder( HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }


    @GetMapping("/image/{fileName}")
    public ResponseEntity<byte[]> getImgae(@PathVariable String fileName) {
        Path path = Paths.get(categoryService.getUploadDir()).resolve(fileName); // Sử dụng resolve để nối đường dẫn

        if (Files.exists(path)) { // Kiểm tra sự tồn tại của tệp
            try {
                byte[] fileData = Files.readAllBytes(path); // Đọc dữ liệu tệp
                String mimeType = Files.probeContentType(path); // Xác định loại tệp
                MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM_VALUE);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(fileData);
            } catch (IOException e) {
                e.printStackTrace(); // In ra lỗi để kiểm tra chi tiết
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Trả về lỗi máy chủ
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Trả về lỗi không tìm thấy tệp
        }
    }
    @PutMapping("/{idCategory}")
    public ResponseEntity<?> updateCategoryDetails(

            @PathVariable("idCategory") Integer idCategory,
            @RequestParam(value = "nameCategory", required = false) String nameCategory,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) {
        try {
            // Kiểm tra xem nameCategory có tồn tại hay không
            if (nameCategory == null || nameCategory.trim().isEmpty()) {
                return ResponseHandler.responeBuilder(HttpStatus.OK, false, "Category name cannot be empty");
            }

            // Kiểm tra xem idCategory có tồn tại hay không
            Category existingCategory = categoryService.getCategory(idCategory);
            if (existingCategory == null) {
                // Nếu không tồn tại, ném ngoại lệ ResourceNotFoundException
                throw new ResourceNotFoundException("Category with ID " + idCategory + " not found");
            }

            // Nếu file có, gọi service để cập nhật cả tên và ảnh, nếu không, chỉ cập nhật tên
            String result;
            if (file != null && !file.isEmpty()) {
                result = categoryService.updateCategory(idCategory, nameCategory, file);
            } else {
                result = categoryService.updateCategoryNameOnly(idCategory, nameCategory);
            }

            // Trả về phản hồi thành công với thông điệp từ service
            return ResponseHandler.responeBuilder(HttpStatus.OK, true, result);

        } catch (ResourceNotFoundException e) {
            // Xử lý trường hợp danh mục không tồn tại
            return ResponseHandler.responeBuilder(HttpStatus.NOT_FOUND, false, e.getMessage());

        } catch (Exception e) {
            // Xử lý các ngoại lệ khác nếu có
            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, "An error occurred while updating the category");
        }
    }

//

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/image-url/{fileName}")
    public ResponseEntity<String> getImageUrl(@PathVariable String fileName) {
        String baseUrl = "http://localhost:8080"; // Thay thế bằng URL của bạn nếu cần
        String imageUrl = baseUrl + "/category/picture/" + fileName;
        return ResponseEntity.ok(imageUrl);
    }

    private CategoryRespone convertToCategoryResponse(Category category) {
        String baseUrl = "http://localhost:8080/category/image/";
        String imageUrl = baseUrl + encodeURIComponent(category.getImage());

        CategoryRespone response = new CategoryRespone();
        response.setIdCategory(category.getIdCategory());
        response.setNameCategory(category.getNameCategory());
        response.setImageUrl(imageUrl);
        response.setCreateAt(category.getCreateAt());
        response.setUpdateAt(category.getUpdateAt());
      //  response.setDeleted(category.isDeleted());
        return response;
    }

    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<?> getCategoryList(
           @RequestParam(value = "nameCategory", required = false) String nameCategory,
           @RequestParam(required = false) String categoryCode,
           @RequestParam(required = false)
           @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime startDate,
           @RequestParam(required = false)
           @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Category> categoryPage = categoryService.getList(categoryCode,nameCategory,page, size,startDate,endDate);

        if (categoryPage.isEmpty()) {
            return ResponseHandler.responeBuilder( HttpStatus.OK, false, "No categories found");
        } else {
            List<CategoryRespone> categoryResponses = categoryPage.getContent().stream()
                    .map(this::convertToCategoryResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("categories", categoryResponses);
            response.put("currentPage", categoryPage.getNumber());
            response.put("totalItems", categoryPage.getTotalElements());
            response.put("totalPages", categoryPage.getTotalPages());
            //   response.put("currentPage", categoryPage.getNumber());

            return ResponseHandler.responeBuilder( HttpStatus.OK, true, response);
        }
    }
    @DeleteMapping("/{idCategory}")
    public ResponseEntity<?> deleteCategory(@PathVariable("idCategory") Integer idCategory) {
        try {
            categoryService.markCategoryAsDeleted(idCategory);
            return ResponseHandler.responeBuilder( HttpStatus.OK, true, null);
        } catch (IllegalStateException e) {
            return ResponseHandler.responeBuilder(HttpStatus.OK,false,e.getMessage());
        }
    }
}

