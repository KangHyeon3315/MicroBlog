package com.microblog.composite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.microblog")
public class CompositeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompositeApplication.class, args);
	}

}
