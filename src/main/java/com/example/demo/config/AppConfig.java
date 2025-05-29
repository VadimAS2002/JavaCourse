package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @PostConstruct
    public void init() {
        System.out.println("Datasource URL: " + datasourceUrl);
    }
}
