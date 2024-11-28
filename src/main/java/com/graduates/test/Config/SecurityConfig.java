package com.graduates.test.Config;


import com.graduates.test.service.impl.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private  JwtAuthenticationFilter jwtTokenFilter;


    public SecurityConfig(CustomerUserDetailsService customerUserDetailsService) {
        this.customerUserDetailsService = customerUserDetailsService;
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JWT filter trước filter của Username/Password
//                .authorizeRequests(requests -> requests
//                        .requestMatchers("/user/auth/**").permitAll() // Cho phép truy cập không cần xác thực
//                        .requestMatchers("/category/**").permitAll()
//                    //    .requestMatchers("/category/list/**").permitAll()
//                        .requestMatchers("/cart/**").permitAll()
//                        .requestMatchers("/book/**").permitAll()
//                        .requestMatchers("/book/list/**").permitAll()
//                        .requestMatchers("/publisher/**").permitAll()
//                        .requestMatchers("/distributor/**").permitAll()
//                        .requestMatchers("/orders/**").permitAll()
//                    //    .requestMatchers("/orders/update-status/**").permitAll()
//                        .requestMatchers("/orders/list/admin").hasRole("ADMIN")
//                   //     .requestMatchers("/orders/detail_admin/**").permitAll()
//                        .requestMatchers("/feedback/**").permitAll()
//                        .requestMatchers("/feedback/list").hasRole("ADMIN")
//
//                        .anyRequest().authenticated() // Mọi yêu cầu khác cần phải xác thực
//                );
//        return http.build();
//    }
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Cấu hình CORS
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JWT filter trước filter của Username/Password
            .authorizeRequests(requests -> requests
                    .requestMatchers("/user/auth/**").permitAll()
                    .requestMatchers("/category/**").permitAll()
                    .requestMatchers("/cart/**").permitAll()
                    .requestMatchers("/book/**").permitAll()
                    .requestMatchers("/publisher/**").permitAll()
                    .requestMatchers("/distributor/**").permitAll()
                    .requestMatchers("/orders/**").permitAll()
                    .requestMatchers("/orders/list/admin").hasRole("ADMIN")
                    .requestMatchers("/feedback/**").permitAll()
                    .requestMatchers("/feedback/list").hasRole("ADMIN")
                    .anyRequest().authenticated() // Mọi yêu cầu khác cần phải xác thực
            );
    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // Cho phép domain frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Hoặc có thể chỉ định các header cụ thể
        configuration.setAllowCredentials(true); // Cho phép gửi cookie hoặc header authorization
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình AuthenticationProvider trong Spring Security giúp xác thực người dùng bằng cách
     * sử dụng UserDetailsService và PasswordEncoder
     */
    @Bean
    protected AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customerUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Bean cho SecurityFilterChain
     * Sử dụng SecurityFilterChain cấu hình cho Spring Security
     * Bao gồm phân quyền các endpoint
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(r -> {
                    r.anyRequest().permitAll();
                });
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults()) // Kích hoạt CORS
//                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(r -> {
//                    r.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); // Cho phép OPTIONS
//                    r.anyRequest().permitAll(); // Cho phép tất cả yêu cầu khác
//                });
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // URL frontend
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Phương thức được phép
//        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization")); // Header được phép
//        configuration.setAllowCredentials(true); // Cookie hoặc thông tin xác thực
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho tất cả endpoint
//        return source;
//    }
}