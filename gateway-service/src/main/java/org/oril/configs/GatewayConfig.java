package org.oril.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
//                .route("user-service", r -> r.path("/users/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .route("snippet-service", r -> r.path("/snippets/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://snippet-service"))
                .route("photo-upload-service", r -> r.path("/posts/*/photos/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://photo-upload-service"))
                .route("recommendation-service", r -> r.path("/posts/recommendations")
                        .filters(f -> f.filter(filter))
                        .uri("lb://recommendation-service"))
                .route("post-upload-service", r -> r.path("/posts/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://post-upload-service"))
                .route("friend-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://friend-service"))
                // Add more routes here based on the configuration in application.yml
                .build();
    }
}