package com.graduates.test.controller;

import com.graduates.test.dto.*;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.*;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.resposity.CartRepository;
import com.graduates.test.resposity.RoleRespository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.UserService;
import com.graduates.test.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/auth")
public class AuthorController {
    private AuthenticationManager authenticationManager;
    private UserResposity userResposity;
    private RoleRespository roleRespository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private CartRepository cartRepository;

    public AuthorController(AuthenticationManager authenticationManager, UserResposity userResposity, RoleRespository roleRespository, PasswordEncoder passwordEncoder, UserService userService, CartRepository cartRepository) {
        this.authenticationManager = authenticationManager;
        this.userResposity = userResposity;
        this.roleRespository = roleRespository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/register")

    public ResponseEntity<?> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("fullname") String fullname,
            @RequestParam("dob") String dob,
            @RequestParam("phone") String phone,
            @RequestParam("street") String street,
            @RequestParam("city") String city
    ) {
        if (userResposity.existsByUsername(username)) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false, "Username already exists");
        }
        // Tạo UserEntity
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role roles = roleRespository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(roles));
        user.setEmail(email);
        user.setFullname(fullname);
        user.setDob(dob);
        user.setPhone(phone);

        // Tạo Address và gán vào user
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);

        user.setAddress(address);
        userResposity.save(user);
        Cart cart = new Cart();
        cart.setUser(user);

        // Lưu Cart vào cơ sở dữ liệu
        cartRepository.save(cart);

        // Lưu vào database qua JPA repository
        //  userResposity.save(user);

        // return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, null);
    }

    @PostMapping("admin/register")

    public ResponseEntity<?> register1(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("fullname") String fullname,
            @RequestParam("dob") String dob,
            @RequestParam("phone") String phone,
            @RequestParam("street") String street,
            @RequestParam("city") String city
    ) {
        if (userResposity.existsByUsername(username)) {
            return ResponseHandler.responeBuilder(HttpStatus.OK, false, "Username already exists");
        }
        // Tạo UserEntity
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role roles = roleRespository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singletonList(roles));
        user.setEmail(email);
        user.setFullname(fullname);
        user.setDob(dob);
        user.setPhone(phone);

        // Tạo Address và gán vào user
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);

        user.setAddress(address);

        // Lưu vào database qua JPA repository
        userResposity.save(user);
        Cart cart = new Cart();
        cart.setUser(user);

        // Lưu Cart vào cơ sở dữ liệu
        cartRepository.save(cart);

        // return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, null);
    }

//    @PostMapping("/login1")
//    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
//                loginDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>("User sigin success", HttpStatus.OK);
//
//
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Ép kiểu thành CustomUserDetails
//            CustomUserDetails loggedInUser = (CustomUserDetails) authentication.getPrincipal();
//
//            // Trả về thông tin người dùng và vai trò của họ
//            UserEntity userEntity = loggedInUser.getUserEntity();
//
//            // Chuyển đổi sang DTO
//            UserResponseDTO responseDTO = convertToDTO(userEntity);
//
//            return ResponseHandler.responeBuilder(HttpStatus.OK, true, responseDTO);
//        } catch (BadCredentialsException e) {
//            return ResponseHandler.responeBuilder(HttpStatus.OK, false, null);
//        } catch (Exception e) {
//            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, null);
//        }
//    }

    private UserResponseDTO convertToDTO(UserEntity userEntity) {
        UserResponseDTO response = new UserResponseDTO();
        response.setIdUser(userEntity.getIdUser());
        response.setUsername(userEntity.getUsername());
        response.setEmail(userEntity.getEmail());
        response.setFullname(userEntity.getFullname());
        response.setDob(userEntity.getDob());
        response.setPhone(userEntity.getPhone());

        // Lấy địa chỉ từ thực thể User
        if (userEntity.getAddress() != null) {
            response.setAddress(userEntity.getAddress().getStreet() + ", " +
                    userEntity.getAddress().getCity()
            );
        } else {
            response.setAddress(null); // Nếu không có địa chỉ
        }

        // Lấy danh sách vai trò của người dùng
        List<String> roles = userEntity.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        response.setRoles(roles);

        return response;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "dob", required = false) String dob,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sizes", defaultValue = "10") int sizes) {

        // Giả sử bạn có một phương thức trong userService để lấy danh sách người dùng theo các tham số lọc
        Page<UserEntity> userPage = userService.searchUser(username, email, fullname, dob, phone, street, city, page, sizes);

        // Chuyển đổi danh sách người dùng thành DTO
        List<UserResponseDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Tạo phản hồi với thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("users", userDTOs);
        response.put("currentPage", userPage.getNumber());
        response.put("totalUser", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());

        //   return ResponseEntity.ok(response);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, response);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles(@RequestParam Integer userId) {
        List<String> roles = userService.getRolesByUserId(userId);
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, roles);
    }


    @GetMapping("/list/user")
    public ResponseEntity<?> getUsersByRole() {
        List<UserEntity> users = userService.getUsersByRole("ROLE_USER");
        return ResponseHandler.responeBuilder(HttpStatus.OK, true, users);

    }

    @PutMapping("/update_info")
    public ResponseEntity<?> updateCategoryDetails(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "dob", required = false) String dob,
            @RequestParam(value = "phone", required = false) String phone
    ) {
        try {
            // Kiểm tra xem nameCategory có tồn tại hay không
            token = token.replace("Bearer ", "");

            userService.updateAccount(token, fullname, email, dob, phone);
            return ResponseHandler.responeBuilder(HttpStatus.OK, true, "User information updated successfully");

        } catch (ResourceNotFoundException e) {
            // Xử lý trường hợp danh mục không tồn tại
            return ResponseHandler.responeBuilder(HttpStatus.NOT_FOUND, false, e.getMessage());

        } catch (Exception e) {
            // Xử lý các ngoại lệ khác nếu có
            return ResponseHandler.responeBuilder(HttpStatus.INTERNAL_SERVER_ERROR, false, "An error occurred while updating the user");
        }
    }



    @PostMapping("/login-token")
    public ResponseEntity<?> createToken(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        TokenDTO tokenDTO = userService.login(username, password);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,tokenDTO);
                //ResponseEntity.ok().body(tokenDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @RequestParam("refreshToken") String refreshToken
    ) {
        RefreshTokenDTO refreshTokenDTO = userService.refreshToken(refreshToken);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,refreshTokenDTO);
                //ResponseEntity.ok().body(refreshTokenDTO);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfileUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        UserDto refreshTokenDTO = userService.getProfileUser(authorizationHeader);
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,refreshTokenDTO);
                //ResponseEntity.ok().body(refreshTokenDTO);

    }
}

