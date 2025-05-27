package com.ssafy.pjt;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SsafyTripFinalKimJoonHyeokParkJiHunBongHyenJinApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();
    System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
		System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));

		SpringApplication.run(SsafyTripFinalKimJoonHyeokParkJiHunBongHyenJinApplication.class, args);
	}

}
