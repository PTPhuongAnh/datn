package com.graduates.test.service.impl;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.CartResponse;
import com.graduates.test.model.Book;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import com.graduates.test.model.UserEntity;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.CartDetailRepository;
import com.graduates.test.resposity.CartRepository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    private BookCategoryResposity bookRepository;
    private UserResposity userResposity;
    private CartDetailRepository cartDetailRepository;

    private JwtService jwtService;

    public CartServiceImpl(CartRepository cartRepository, BookCategoryResposity bookRepository, UserResposity userResposity, CartDetailRepository cartDetailRepository, JwtService jwtService) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userResposity = userResposity;
        this.cartDetailRepository = cartDetailRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void addToCartWithToken(String token, Integer bookId, int quantity) throws Exception {
        // Trích xuất username từ token
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));

        // Tìm user dựa trên username
        UserEntity user = userResposity.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));

        // Gọi phương thức addToCart với userId lấy được
        addToCart(user.getIdUser(), bookId, quantity);
    }

    @Override
    public void addToCart(Integer userId, Integer bookId, int quantity) throws Exception {

        Optional<UserEntity> optionalUser = userResposity.findById(userId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        if (optionalBook.isEmpty()) {
            throw new Exception("Book not found");
        }

        UserEntity user = optionalUser.get();
        Book book = optionalBook.get();

        if (book.getQuantity() <= 0) {
            throw new IllegalArgumentException("Không thể thêm vào giỏ hàng. Sản phẩm hiện không còn hàng.");
        }

        Cart cart = user.getCart();
        // Kiểm tra xem sách đã tồn tại trong giỏ hàng và chưa bị xóa mềm
        Optional<CartDetail> existingCartDetailOpt = cart.getCartDetails().stream()
                .filter(cartDetail -> cartDetail.getBook().getIdBook().equals(book.getIdBook()) && !cartDetail.isDeleted() && !cartDetail.isPurchased())
                .findFirst();

        if (existingCartDetailOpt.isPresent()) {
            CartDetail existingCartDetail = existingCartDetailOpt.get();
            int newQuantity = existingCartDetail.getQuantity() + quantity;

            // Kiểm tra xem tổng số lượng mới có vượt quá số lượng trong kho không
            if (newQuantity > book.getQuantity()) {
                throw new IllegalArgumentException("Số lượng muốn thêm vượt quá số lượng trong kho!");
            }

            existingCartDetail.setQuantity(newQuantity); // Cập nhật số lượng
        } else {
            // Nếu sách đã tồn tại nhưng bị xóa mềm (delete = true)
            Optional<CartDetail> softDeletedCartDetailOpt = cart.getCartDetails().stream()
                    .filter(cartDetail -> cartDetail.getBook().getIdBook().equals(book.getIdBook()) && cartDetail.isDeleted() && !cartDetail.isPurchased())
                    .findFirst();

            if (softDeletedCartDetailOpt.isPresent()) {
                CartDetail softDeletedCartDetail = softDeletedCartDetailOpt.get();

                // Cập nhật lại delete = false và số lượng
                if (quantity > book.getQuantity()) {
                    throw new IllegalArgumentException("Số lượng muốn thêm vượt quá số lượng sách trong kho!");
                }
                softDeletedCartDetail.setDeleted(false);
                softDeletedCartDetail.setQuantity(quantity);
            } else {
                // Nếu sách chưa có, kiểm tra số lượng muốn thêm
                if (quantity > book.getQuantity()) {
                    throw new IllegalArgumentException("Số lượng muốn thêm vượt quá số lượng sách trong kho!");
                }
                // Tạo chi tiết giỏ hàng mới
                CartDetail cartDetail = new CartDetail(cart, book, quantity);
                cart.addCartDetail(cartDetail);
            }
        }

        cart.updateTotalPrice();
        cartRepository.save(cart);
        userResposity.save(user);
    }

    @Override
    public List<Cart> findByUser_idUser(Integer userId) {
        return null;
    }
    @Override
    public Page<CartResponse> getCartByUserToken(String token, Pageable pageable) throws Exception {
        // Trích xuất username từ token
        String username = jwtService.extractUsername(token);  // jwtUtil là lớp hỗ trợ xử lý JWT

        // Tìm userId dựa trên username
        UserEntity user = userResposity.findByUsername(username)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng với tên đăng nhập: " + username));
        Integer userId = user.getIdUser();

        // Tìm giỏ hàng dựa trên userId
        Cart cart = cartRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new Exception("Không tìm thấy giỏ hàng của người dùng với ID: " + userId));
        Page<CartDetail> cartDetailsPage = cartDetailRepository.findByCartAndIsDeletedFalseAndIsPurchasedFalse(cart, pageable);

        return cartDetailsPage.map(this::convertToCartResponse);
    }

//    @Override
//    public Page<CartResponse> getCartByUserId(Integer userId, Pageable pageable) throws Exception {
//        // Tìm giỏ hàng dựa trên userId
//        Cart cart = cartRepository.findByUserIdUser(userId)
//                .orElseThrow(() -> new Exception("Không tìm thấy giỏ hàng của người dùng với ID: " + userId));
//        Page<CartDetail> cartDetailsPage = cartDetailRepository.findByCartAndIsDeletedFalseAndIsPurchasedFalse(cart, pageable);
//        return cartDetailsPage.map(this::convertToCartResponse);
//    }

    private CartResponse convertToCartResponse(CartDetail cartDetail) {
        Book book = cartDetail.getBook();
        List<String> imageUrls = getImageUrlsFromBook(book);
        Cart cart = cartDetail.getCart();

        return new CartResponse(
                cartDetail.getId(),
                book.getIdBook(),
                book.getNameBook(),
                book.getAuthor(),
                book.getDescription_short(),
                cartDetail.getQuantity(),
                cartDetail.getPrice(),
                imageUrls,
                book.getPrice() * cartDetail.getQuantity()


        );
    }

    private List<String> getImageUrlsFromBook(Book book) {
        String baseUrl = "http://localhost:8080/book/image/";
        return book.getImageBooks().stream()
                .map(image -> baseUrl + encodeURIComponent(image.getImage_url()))
                .collect(Collectors.toList());
    }

    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

//    public void updateCartQuantity(Integer userId, Integer bookId, String operation) throws Exception {
//
//        Cart cart = cartRepository.findByUserIdUser(userId)
//                .orElseThrow(() -> new Exception("Không tìm thấy giỏ hàng của người dùng với ID: " + userId));
//        if (!cart.getUser().getIdUser().equals(userId)) {
//            throw new Exception("Giỏ hàng không thuộc về người dùng với ID: " + userId);
//        }
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new Exception("Không tìm thấy sách với ID: " + bookId));
//
//        CartDetail cartDetail = cartDetailRepository.findByCartAndBook(cart, book)
//                .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm trong giỏ hàng với ID sách: " + bookId));
//
//        if (operation.equals("increase")) {
//            if (book.getQuantity() <= cartDetail.getQuantity()) {
//                throw new Exception("Không đủ số lượng trong kho cho sách: " + book.getNameBook());
//            }
//            cartDetail.setQuantity(cartDetail.getQuantity() + 1);
//        } else if (operation.equals("decrease")) {
//            if (cartDetail.getQuantity() == 0) {
//                cartDetailRepository.delete(cartDetail); // Xóa sản phẩm nếu số lượng = 1
//                return;
//            }
//            cartDetail.setQuantity(cartDetail.getQuantity() - 1);
//        } else {
//            throw new Exception("Operation không hợp lệ: " + operation);
//        }
//
//        cartDetailRepository.save(cartDetail);
//    }
//

    public void updateCartQuantity(String token, Integer bookId, String operation) throws Exception {
        // Trích xuất username từ token
        String username = jwtService.extractUsername(token);  // Lấy username từ token

        // Tìm userId dựa trên username
        UserEntity user = userResposity.findByUsername(username)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng với username: " + username));
        Integer userId = user.getIdUser();  // Lấy userId từ đối tượng UserEntity

        // Tiến hành xử lý như cũ
        Cart cart = cartRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new Exception("Không tìm thấy giỏ hàng của người dùng với ID: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new Exception("Không tìm thấy sách với ID: " + bookId));

        CartDetail cartDetail = cartDetailRepository.findByCartAndBook(cart, book)
                .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm trong giỏ hàng với ID sách: " + bookId));

        if (operation.equals("increase")) {
            if (book.getQuantity() <= cartDetail.getQuantity()) {
                throw new Exception("Không đủ số lượng trong kho cho sách: " + book.getNameBook());
            }
            cartDetail.setQuantity(cartDetail.getQuantity() + 1);
        } else if (operation.equals("decrease")) {
            if (cartDetail.getQuantity() == 0) {
                cartDetailRepository.delete(cartDetail); // Xóa sản phẩm nếu số lượng = 0
                return;
            }
            cartDetail.setQuantity(cartDetail.getQuantity() - 1);
        } else {
            throw new Exception("Operation không hợp lệ: " + operation);
        }

        cartDetailRepository.save(cartDetail);
    }

    public void deleteBooksFromCart(String token, List<Integer> idBooks) throws Exception {
        String username = jwtService.extractUsername(token);  // Lấy username từ token

        // Tìm userId dựa trên username
        UserEntity user = userResposity.findByUsername(username)
                .orElseThrow(() -> new Exception("Không tìm thấy người dùng với username: " + username));
        Integer userId = user.getIdUser();  // Lấy userId từ đối tượng UserEntity
        Cart cart = cartRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new IllegalStateException("Cart not found for user ID: " + userId));

        // Lặp qua danh sách idBooks và đánh dấu các CartDetail tương ứng là đã xóa
        for (Integer idBook : idBooks) {
            // Lấy danh sách các chi tiết giỏ hàng khớp với bookId và cartId
            List<CartDetail> cartDetails = cartDetailRepository.findAllByBook_IdBookAndCart_IdCart(idBook, cart.getIdCart());

            if (cartDetails.isEmpty()) {
                throw new IllegalStateException("Book with ID " + idBook + " not found in user's cart.");
            }

            // Lặp qua danh sách và đánh dấu là đã xóa
            for (CartDetail cartDetail : cartDetails) {
                // Kiểm tra quyền sở hữu giỏ hàng
                if (!cart.getUser().getIdUser().equals(userId)) {
                    throw new IllegalStateException("You are not authorized to delete this item from the cart.");
                }

                // Đánh dấu chi tiết giỏ hàng là đã xóa
                cartDetail.setDeleted(true);

                // Lưu thay đổi
                cartDetailRepository.save(cartDetail);
            }
        }
    }
}





