package com.graduates.test.service.impl;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.RefreshTokenDTO;
import com.graduates.test.dto.TokenDTO;
import com.graduates.test.dto.UpdateUserRequest;
import com.graduates.test.dto.UserDto;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.RoleRespository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.graduates.test.model.Role;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserResposity userRepository;
    @Autowired
    private RoleRespository roleRespository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;




    @Value("${file.upload-dir}")
    private String uploadDir;


    public List<String> getRolesByUserId(Integer userId) {
        // Tìm người dùng theo userId
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Trả về danh sách tên vai trò của người dùng
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }


    @Override
    public Page<UserEntity> searchUser(String username, String email, String fullname, String dob, String phone, String street, String city, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
        return userRepository.searchUser(username, email, fullname, dob, phone, street,city,pageable);
    }

    @Override
    public List<UserEntity> getUsersByRole(String roleName) {
            return userRepository. findByRoles_Name(roleName);
        }

    public boolean isAdmin(Integer userId) {
        // Lấy thông tin người dùng từ userId
        UserEntity user = userRepository.findById(userId).orElse(null);

        // Kiểm tra nếu user tồn tại và có role là "ROLE_ADMIN"
        if (user != null && user.getRoles() != null) {
            return user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        }

        return false;
    }


    public String updateUser(Integer userId, UpdateUserRequest updateUserRequest) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        UserEntity user = userOpt.get();

        // Cập nhật các thông tin
        if (updateUserRequest.getUsername() != null) {
            user.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getPhone() != null) {
            user.setPhone(updateUserRequest.getPhone());
        }
        if (updateUserRequest.getDob() != null) {
            user.setDob(updateUserRequest.getDob());
        }

        userRepository.save(user);
        return "User updated successfully";
    }


    @Override
    public UserEntity getUser(Integer idUser) {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with ID " + idUser + " not found"));

    }
    @Override
    public String updateAccount(int idUser, String fullname,String email, String dob, MultipartFile file) {
        Optional<UserEntity> existingCategory = userRepository.findById(idUser);
        if (existingCategory.isPresent()) {
            UserEntity updatedCategory = existingCategory.get();
            updatedCategory.setFullname(fullname);
            updatedCategory.setEmail(email);
            updatedCategory.setDob(dob);

            if (file != null && !file.isEmpty()) {
                String fileName = saveImage(file);
                updatedCategory.setImage(fileName);
            }

           userRepository.save(updatedCategory);
            return "Update category success";
        } else {
            return "Category not found";
        }
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

    @Override
    public RefreshTokenDTO refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserEntity user = userRepository.findAllByUsername(username);
        if (jwtService.validateToken(refreshToken, new CustomUserDetails(userRepository.findAllByUsername(username)))) {
            String accessToken = jwtService.generateAccessToken(Objects.requireNonNull(user), JwtService.REFRESH_TOKEN_EXPIRATION);
            return new RefreshTokenDTO(accessToken);
        } else {
            throw new BadCredentialsException("token-is-invalid");
        }
    }

    @Override
    public TokenDTO login(String username, String password) {
        UserEntity user = userRepository.findAllByUsername(username);
        CustomUserDetails customerUserDetail = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password, customerUserDetail.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        String tokenUser = jwtService.generateAccessToken(user, JwtService.ACCESS_TOKEN_EXPIRATION);
        String refreshToken = jwtService.generateAccessToken(user, JwtService.REFRESH_TOKEN_EXPIRATION);
        return new TokenDTO(tokenUser, refreshToken);
    }

    @Override
    public UserDto getProfileUser(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        UserEntity entity = userRepository.findAllByUsername(username);
//        List<RoleDto> roleDtos = roleRespository.findAllByIdRoles();
        return UserDto.builder()
                .idUser(entity.getIdUser())
                .username(entity.getUsername())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .dob(entity.getDob())
                .roles(entity.getRoles())
                .build();
    }

}




