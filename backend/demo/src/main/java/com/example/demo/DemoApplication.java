package com.example.demo;

import com.example.demo.model.request.StoreRequest;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Manage a motorbike repair shop", version = "1.0.0",

		description = "Good Website", termsOfService = "runcodenow", contact = @Contact(name = "Anonymous", email = "dev.team@gmail.com"), license = @License(name = "license", url = "runcodenow")))
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	@Autowired
	private StoreService service;

	@Autowired
	private StoreRepository storeRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!storeRepository.findById(1L).isPresent()) {
			StoreRequest storeRequest = StoreRequest.builder()
					.address(null)
					.phone(null)
					.email(null)
					.name(null)
					.vat(null)
					.build();
			service.create(storeRequest);
		}
	}
}