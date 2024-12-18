package com.example.walkthru;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalkthruRoutingApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().directory("walkthru/config").ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(WalkthruRoutingApplication.class, args);
	}
}
