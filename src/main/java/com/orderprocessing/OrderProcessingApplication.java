package com.orderprocessing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Gabor Tavali
 */
@SpringBootApplication
public class OrderProcessingApplication implements CommandLineRunner {

    public static void main(String[] args) {

       SpringApplication.run(OrderProcessingApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
