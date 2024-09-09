package com.example.vitamate.config;

import com.example.vitamate.service.OpenApiCommandService;
import com.example.vitamate.service.SupplementService.SupplementCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
public class OpenApiBaseDataConfig {

    @Value("${api.key}")
    private String apiKey;

    @Bean
    CommandLineRunner initData(OpenApiCommandService openApiCommandService){
        return args -> {
            log.info("CommandLineRunner is running");
            log.info("API Key from environment: {}", apiKey);
                try {
                    openApiCommandService.callApiAndSaveData(apiKey, "50", "55");
                    log.info("OPEN_API_BASE_DATA SUCCESS!");
                } catch (Exception e){
                    log.error("OPEN_API_BASE_DATA FAIL!", e);
                }
        };
    }
}
