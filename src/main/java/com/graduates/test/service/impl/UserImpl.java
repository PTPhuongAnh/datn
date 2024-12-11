package com.graduates.test.service.impl;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.*;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Address;
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
    public Page<UserEntity> searchEmployee(String username, String email, String fullname, String dob, String phone, String street, String city, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
        return userRepository.searchEmployee(username, email, fullname, dob, phone, street,city,pageable);
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(String roleName) {

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

    @Override
    public UserEntity getUser(Integer idUser) {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with ID " + idUser + " not found"));

    }
    @Override
    public String updateAccount(String token, String fullname,String email, String dob,String phone) throws Exception {
        String username = jwtService.extractUsername(token);  // Lấy username từ token

        // Tìm userId dựa trên username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng với username: " + username));
        Integer userId = user.getIdUser();  // Lấy userId từ đối tượng UserEntity

        Optional<UserEntity> existingCategory = userRepository.findById(userId);
        if (existingCategory.isPresent()) {
            UserEntity updatedCategory = existingCategory.get();
            updatedCategory.setFullname(fullname);
            updatedCategory.setEmail(email);
            updatedCategory.setDob(dob);
            updatedCategory.setPhone(phone);

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
    public void disableAccount(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
//if(user.getDisable()==true) {
//    user.setDisable(false);  // Đặt trạng thái tài khoản là không hoạt động
//}else{
//    user.setDisable(true);
//}
        user.setDisable(!user.getDisable());
        userRepository.save(user);  // Lưu lại vào cơ sở dữ liệu
    }
    @Override
    public TokenDTO login(String username, String password) {
        UserEntity user = userRepository.findAllByUsername(username);
        if ( user.getDisable()==false) {
            throw new RuntimeException("Account is disabled ");
        }
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
        return UserDto.builder()
                .idUser(entity.getIdUser())
                .username(entity.getUsername())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .dob(entity.getDob())
                .roles(entity.getRoles())
                .address(entity.getAddress().getStreet()+","+entity.getAddress().getCity())
                .build();
    }



}




