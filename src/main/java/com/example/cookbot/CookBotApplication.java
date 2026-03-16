package com.example.cookbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Точка входа в приложение CookBot.
 * <p>
 * Включает:
 * <ul>
 *     <li>Кэширование (Caffeine)</li>
 *     <li>Асинхронную обработку задач (для импорта и отправок бота)</li>
 * </ul>
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class CookBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookBotApplication.class, args);
    }
}