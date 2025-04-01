package com.BudgetEase;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgetEaseApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Loads variables from .env
        System.out.println("Dotenv loaded: " + dotenv.entries().size() + " entries found.");
        // System.out.println("MONGODB_URI: " + dotenv.get("MONGODB_URI"));
        // System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI")); // Make it available to Spring
        SpringApplication.run(BudgetEaseApplication.class, args);
    }
}

