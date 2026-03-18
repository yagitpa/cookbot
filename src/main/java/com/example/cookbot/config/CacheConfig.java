package com.example.cookbot.config;

import com.example.cookbot.util.Constants;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Конфигурация кэширования на основе Caffeine.
 * <p>
 * Настраивает локальный in-memory кэш для хранения результатов поиска рецептов и популярных рецептов.
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see Constants.Cache
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Создает CacheManager с настройками Caffeine.
     * <p>
     * Параметры:
     * <ul>
     *     <li>maximumSize - максимальное количество записей в кэше</li>
     *     <li>expireAfterWrite - время жизни записи после записи</li>
     * </ul>
     *
     * @return настроенный CacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(Constants.Cache.CACHE_MAX_SIZE)
                        .expireAfterWrite(Constants.Cache.CACHE_TTL_MINUTES, TimeUnit.MINUTES)
                        .recordStats() // Включение статистики для мониторинга
        );

        return cacheManager;
    }
}