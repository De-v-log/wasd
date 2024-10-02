package com.wasd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/dev.properties")
public class PropertiesConfig {
}
