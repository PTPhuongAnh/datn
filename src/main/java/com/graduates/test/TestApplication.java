package com.graduates.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
