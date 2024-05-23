package ru.micro.config;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.yml")
@org.springframework.context.annotation.Configuration
public class Neo4jConfig {
    @Bean
    Configuration cypherDslConfiguration() {
        return Configuration.newConfig()
                .withDialect(Dialect.NEO4J_5).build();
    }
}