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
                .requestMatchers(HttpMethod.DELETE, "/category/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/category/create").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/category/{idCategory}").hasRole("ADMIN")
                .requestMatchers("/book/list").permitAll()
                .requestMatchers("/publisher/list").permitAll()
                .requestMatchers("/distributor/list").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/book/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/book/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/book/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/publisher/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/publisher/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/publisher/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/distributor/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/distributor/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/distributor/**").hasRole("ADMIN")
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