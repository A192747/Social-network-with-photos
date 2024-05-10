package ru.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class PhotoUploadService {

    public static void main(String[] args) {
        SpringApplication.run(PhotoUploadService.class, args);
    }
}
