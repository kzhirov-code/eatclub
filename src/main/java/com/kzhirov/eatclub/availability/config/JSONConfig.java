package com.kzhirov.eatclub.availability.config;

import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class JSONConfig {
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
