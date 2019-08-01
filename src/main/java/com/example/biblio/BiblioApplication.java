package com.example.biblio;

import com.example.biblio.util.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class BiblioApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblioApplication.class, args);
	}

}
