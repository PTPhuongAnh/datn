package com.graduates.test.service.impl;

import com.graduates.test.model.Book;
import com.graduates.test.model.Cart;
import com.graduates.test.model.CartDetail;
import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.CartRepository;
import com.graduates.test.resposity.UserResposity;
import com.graduates.test.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    private BookCategoryResposity bookRepository;
    private UserResposity userResposity;

    public CartServiceImpl(CartRepository cartRepository, BookCategoryResposity bookRepository, UserResposity userResposity) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userResposity = userResposity;
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
        Cart cart = user.getCart();

        // Nếu người dùng chưa có giỏ hàng, tạo mới giỏ hàng
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart); // Thiết lập giỏ hàng cho người dùng
        }

        Book book = optionalBook.get();

        // Kiểm tra xem sách đã có trong giỏ hàng chưa
        boolean bookExistsInCart = cart.getCartDetails().stream()
                .anyMatch(cartDetail -> cartDetail.getBook().getIdBook().equals(book.getIdBook()));

        if (bookExistsInCart) {
            // Nếu sách đã có, cập nhật số lượng
            cart.getCartDetails().forEach(cartDetail -> {
                if (cartDetail.getBook().getIdBook().equals(book.getIdBook())) {
                    cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                }
            });
        } else {
            // Nếu sách chưa có, tạo chi tiết giỏ hàng mới
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
    public Optional<Cart> getCartByUserId(Integer userId) {
        return userResposity.findById(userId).map(UserEntity::getCart);
    }
}
