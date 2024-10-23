package com.graduates.test;

import com.graduates.test.service.MomoPaymentService;
import com.graduates.test.service.impl.PaymentMoMoImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);

	}
//	@Bean
//	public CommandLineRunner run(PaymentMoMoImpl momoPaymentService) {
//		return args -> {
//			// Gọi phương thức tạo thanh toán MoMo để in chữ ký
//			momoPaymentService.createMomoPayment();
//		};
//	}
}
