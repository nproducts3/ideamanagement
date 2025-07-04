package com.ideamanagement;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IdeamanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeamanagementApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Idea Management API")
						.version("1.0")
						.description("API for managing ideas and database tracking")
						.contact(new Contact()
								.name("Idea Management Team")
								.email("support@ideamanagement.com"))
						.license(new License()
								.name("Apache 2.0")
								.url("http://www.apache.org/licenses/LICENSE-2.0.html")));
	}

}
