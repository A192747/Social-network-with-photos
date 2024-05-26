package ru.micro.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableConfigurationProperties(CassandraProperties.class)
@EnableCassandraRepositories(
        basePackages = "ru.micro.repository.primary", // Замените на ваш пакет
        cassandraTemplateRef = "primaryCassandraTemplate"
)
public class PrimaryCassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "socialNetworkKeyspace2"; // Укажите ключевое пространство для первого источника данных
    }

    @Bean(name = "primaryCassandraTemplate")
    public CassandraTemplate primaryCassandraTemplate(@Qualifier("primarySession") CqlSessionFactoryBean session) {
        return new CassandraTemplate(session.getObject());
    }

    @Bean(name = "primarySession")
    @ConfigurationProperties(prefix = "spring.data.cassandra.primary")
    public CqlSessionFactoryBean primarySession() {
        return new CqlSessionFactoryBean();
    }
}

