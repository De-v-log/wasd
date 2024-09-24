package com.wasd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/secret.properties")
public class PropertieConfig {
}
