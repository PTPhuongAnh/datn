package com.graduates.test.Config;


import com.graduates.test.service.impl.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    private CustomerUserDetailsService customerUserDetailsService;

    public SecurityConfig(CustomerUserDetailsService customerUserDetailsService) {
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                .requestMatchers("/user/auth/**").permitAll()
                .requestMatchers("/category/list").permitAll()
                .requestMatchers("/cart/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/category/delete").permitAll()
                .requestMatchers(HttpMethod.POST,"/category/create").permitAll()
                .requestMatchers(HttpMethod.PUT,"/category/{idCategory}").permitAll()
                .requestMatchers("/book/list").permitAll()
                .requestMatchers("/publisher/list").permitAll()
                .requestMatchers("/distributor/list").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/book/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/book/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/book/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/publisher/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/publisher/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/publisher/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/distributor/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/distributor/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/distributor/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();


        return http.build();
    }
//    @Bean
//    public UserDetailsService users(){
//        UserDetails admin= User.builder()
//                .username("admin")
//                .password("admin")
//                .roles("ADMNIN")
//                .build();
//        UserDetails user=User.builder()
//                .username("user")
//                .password("user")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin,user);
//
//
//    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}