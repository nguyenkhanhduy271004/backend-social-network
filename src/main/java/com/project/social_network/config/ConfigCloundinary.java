package com.project.social_network.config;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigCloundinary {

  @Bean
  public Cloudinary configKey() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", "dbkgbh9kl");
    config.put("api_key", "446688375759579");
    config.put("api_secret", "td-ndp7wQaeWTPiHSAsI7yRWR68");
    return new Cloudinary(config);
  }
}
