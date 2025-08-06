package com.tave.tavewebsite.global.redis.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisProperties {
    private int port;
    private String host;
    private String password;
    private int maxConnection;
    private int minConnection;
}
