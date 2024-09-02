package com.example.vitamate.config;

import com.example.vitamate.service.OpenApiCommandService;
import com.example.vitamate.service.SupplementService.SupplementCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
public class OpenApiBaseDataConfig {

    private String apiKey;

    @Bean
    CommandLineRunner initData(OpenApiCommandService openApiCommandService){
        return args -> {
            log.info("CommandLineRunner is running");
                try {
                    openApiCommandService.callApiAndSaveData("e72b7472ca414bfc9ce9", "1", "2");
                    log.info("OPEN_API_BASE_DATA SUCCESS!");
                } catch (Exception e){
                    log.error("OPEN_API_BASE_DATA FAIL!");
                }
        };
    }
}
