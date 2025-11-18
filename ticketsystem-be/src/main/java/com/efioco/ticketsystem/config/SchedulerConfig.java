package com.efioco.ticketsystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(3); // numero di thread nel pool
        scheduler.setThreadNamePrefix("scheduler-thread-");

        // log utili e gestione errori
        scheduler.setErrorHandler(ex -> 
            logger.error("❌ Errore in un task schedulato: {}", ex.getMessage(), ex)
        );

        scheduler.initialize();
        return scheduler;
    }
    
    @PostConstruct
    public void init() {
        logger.info("✅ Scheduler attivato correttamente");
    }
}
