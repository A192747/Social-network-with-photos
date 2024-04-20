package ru.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class SnippetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnippetApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
