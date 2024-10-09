package com.graduates.test.service.impl;

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

    public CartServiceImpl(CartRepository cartRepository, BookCategoryResposity bookRepository, UserResposity userResposity, CartDetailRepository cartDetailRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userResposity = userResposity;
        this.cartDetailRepository = cartDetailRepository;
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

        // Kiểm tra xem số lượng sách có lớn hơn 0 không
        if (book.getQuantity() <= 0) {
            throw new IllegalArgumentException("Không thể thêm vào giỏ hàng. Sản phẩm hiện không còn hàng.");
        }

        Cart cart = user.getCart();

        // Nếu người dùng chưa có giỏ hàng, tạo mới giỏ hàng
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart); // Thiết lập giỏ hàng cho người dùng
        }

        // Kiểm tra xem sách đã có trong giỏ hàng chưa
        boolean bookExistsInCart = cart.getCartDetails().stream()
                .anyMatch(cartDetail -> cartDetail.getBook().getIdBook().equals(book.getIdBook()));

        if (bookExistsInCart) {
            // Nếu sách đã có, cập nhật số lượng
            cart.getCartDetails().forEach(cartDetail -> {
                if (cartDetail.getBook().getIdBook().equals(book.getIdBook())) {
                    int newQuantity = cartDetail.getQuantity() + quantity;

                    // Kiểm tra xem tổng số lượng mới có vượt quá số lượng trong kho không
                    if (newQuantity > book.getQuantity()) {

                            throw new IllegalArgumentException("Số lượng muốn thêm vượt quá số lượng còn lại trong sách!");

                    }

                    cartDetail.setQuantity(newQuantity);
                }
            });
        } else {
            // Nếu sách chưa có, kiểm tra số lượng muốn thêm
            if (quantity > book.getQuantity()) {
                throw new IllegalArgumentException("Số lượng muốn thêm vượt quá số lượng còn lại trong sách!");
            }
            // Tạo chi tiết giỏ hàng mới
            CartDetail cartDetail = new CartDetail(cart, book, quantity);
            cart.addCartDetail(cartDetail);
        }

        // Cập nhật tổng giá trị giỏ hàng
        cart.updateTotalPrice();

        // Lưu giỏ hàng vào cơ sở dữ liệu
        cartRepository.save(cart);
        userResposity.save(user); // Cập nhật lại người dùng với giỏ hàng mới
    }

    @Override
    public List<Cart> findByUser_idUser(Integer userId) {
        return null;
    }

    @Override
    public List<CartResponse> getCartByUserId(Integer userId) {
        List<Cart> carts = cartRepository.findByUser_IdUser(userId);
        return carts.stream()
                .flatMap(cart -> cart.getCartDetails().stream()
                        .map(this::convertToCartResponse)) // Chuyển đổi từng CartDetail sang CartResponse
                .collect(Collectors.toList());
    }

    private CartResponse convertToCartResponse(CartDetail cartDetail) {
        Book book = cartDetail.getBook();
        List<String> imageUrls = getImageUrlsFromBook(book);
        return new CartResponse(
                book.getIdBook(),
                book.getNameBook(),
                book.getAuthor(),
                book.getDescription_short(),
                cartDetail.getQuantity(),
                cartDetail.getPrice(),
                imageUrls
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




}
