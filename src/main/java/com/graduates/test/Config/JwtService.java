package com.graduates.test.Config;

import com.graduates.test.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


@Component
public class JwtService {
    public static final String SECRET_KEY = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public static final int ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000;
    public static final int REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
    public static final int TOKEN_EXPIRATION_LINK_RESET_PASSWORD = 15 * 60 * 1000;


    /**
     * Tạo ra JWT từ thông tin user
     * Trả về từ phương thức là chuỗi JWT đã được tạo, chứa thông tin về người dùng và các
     * thông tin thời gian (phát hành, hết hạn)
     * */
    public String generateAccessToken(UserEntity userDTO, int exprirationToken){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + exprirationToken);
        Claims claims = Jwts.claims().setSubject(userDTO.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignWithKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Tạo ra một khóa để sử dụng trong việc ký (sign) chuỗi JWT
     * bằng thuật toán HMAC-SHA256
     * */
    private Key getSignWithKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     *
     * @param token
     * @return Lấy ra danh sách cách Claims từ tokem
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignWithKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * trả về 1 Claim
     * @param token
     * @param claimsResolver dạng của claim
     * @return Claim
     * @param <T>
     */
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Kiểm tra hạn của token
     * @param token
     * @return còn true, hết false
     */
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    /**
     * lấy username
     * @param token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }




    /**
     * Kiểm tra xem token hợp lệ không
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }



}
