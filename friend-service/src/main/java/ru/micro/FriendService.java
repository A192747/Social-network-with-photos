package ru.micro;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class FriendService {

    public static void main(String[] args) {
        SpringApplication.run(FriendService.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
