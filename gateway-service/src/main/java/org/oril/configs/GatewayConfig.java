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
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
<<<<<<< HEAD
                .route("forum-service", r -> r.path("/topic/**", "/message/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://forum-service"))
                .route("snippet-service", r -> r.path("/snippet/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://snippet-service"))
=======
>>>>>>> d9ac06fb3bd24589862d651040e8f7669dc2f7a5
                // Add more routes here based on the configuration in application.yml
                .build();
    }
}