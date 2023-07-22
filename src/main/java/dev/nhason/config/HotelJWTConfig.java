package dev.nhason.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dev.nhason.hotel")
public class HotelJWTConfig {

    String secret;
    private Long expires;

    public HotelJWTConfig() {

    }

    public HotelJWTConfig(String secret, Long expires) {
        this.secret = secret;
        this.expires = expires;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpires() {
        return expires;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }
}
