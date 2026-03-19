package com.example.cookbot.config;

import com.example.cookbot.service.strategy.ExactMatchStrategy;
import com.example.cookbot.service.strategy.FuzzyMatchStrategy;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация стратегий поиска.
 * <p>
 * Стратегии регистрируются как Spring-бины и внедряются в сервисы.
 * </p>
 *
 * @author dev
 * @version 1.0
 */
@Configuration
public class SearchStrategyConfig {

    // Стратегии регистрируются через @Component, этот класс для будущей расширяемости
    // Можно добавить фабрику стратегий или переключение между алгоритмами
}