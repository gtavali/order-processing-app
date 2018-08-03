package com.orderprocessing;

import com.orderprocessing.services.CsvService;
import com.orderprocessing.services.OrderProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * The application file.
 *
 * @author Gabor Tavali
 */
@SpringBootApplication
public class OrderProcessingApplication implements CommandLineRunner {

    @Autowired
    CsvService csvService;

    @Autowired
    OrderProcessingService orderProcessingService;

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File file = new File(args[0]);
        orderProcessingService.processOrder(csvService.readInputFile(file.toPath().toAbsolutePath().toString()));
    }
}
