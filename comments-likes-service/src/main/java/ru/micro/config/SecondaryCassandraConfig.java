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
        basePackages = "ru.micro.repository.secondary",
        cassandraTemplateRef = "secondaryCassandraTemplate"
)
public class SecondaryCassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "socialNetworkKeyspace";
    }

    @Bean(name = "secondaryCassandraTemplate")
    public CassandraTemplate secondaryCassandraTemplate(@Qualifier("secondarySession") CqlSessionFactoryBean session) {
        return new CassandraTemplate(session.getObject());
    }

    @Bean(name = "secondarySession")
    @ConfigurationProperties(prefix = "spring.data.cassandra.secondary")
    public CqlSessionFactoryBean secondarySession() {
        return new CqlSessionFactoryBean();
    }
}

