package org.oril.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("org.oril")
@PropertySource("classpath:database.properties")
public class JdbcConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getProperty("driver"));
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("usernameForDB"));
        dataSource.setPassword(env.getProperty("password"));

        return dataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(){

        return new JdbcTemplate(mysqlDataSource());
    }
}