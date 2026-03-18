package com.example.cookbot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Централизованное хранилище констант приложения.
 * <p>
 * Используется для устранения магических чисел и строк в коде. Все константы сгруппированы по логическим категориям.
 * </p>
 *
 * @author dev
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    /**
     * Константы, связанные с поиском рецептов.
     */
    public static class Search {
        public static final int MAX_RECIPES_LIMIT = 10;
        public static final double MIN_MATCH_PERCENTAGE = 0.5;
        public static final int MAX_ALLOWED_LIMIT = 50;
        public static final String INGREDIENT_SEPARATOR = ",";
        public static final int MIN_INGREDIENT_LENGTH = 2;
        public static final int MAX_INGREDIENT_LENGTH = 50;

        private Search() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    /**
     * Константы, связанные с Telegram ботом.
     */
    public static class Bot {
        public static final String COMMAND_START = "/start";
        public static final String COMMAND_FIND = "/find";
        public static final String CALLBACK_LIKE_PREFIX = "like:";
        public static final String CALLBACK_LIKE_TEMPLATE = "like:recipeId:%d";
        public static final String EMOJI_LIKE = "👍";
        public static final String EMOJI_LIKED = "❤️";
        public static final int MAX_MESSAGE_LENGTH = 4096;

        private Bot() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    /**
     * Константы, связанные с импортом рецептов.
     */
    public static class Import {
        public static final int BATCH_SIZE = 100;
        public static final int MAX_IMPORT_SIZE = 1000;

        private Import() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    /**
     * Константы, связанные с кэшированием.
     */
    public static class Cache {
        public static final String CACHE_RECIPES = "recipes";
        public static final String CACHE_POPULAR = "popularRecipes";
        public static final int CACHE_TTL_MINUTES = 10;
        public static final int CACHE_MAX_SIZE = 500;

        private Cache() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    /**
     * Константы, связанные с API.
     */
    public static class Api {
        public static final String API_BASE_PATH = "/api/v1";
        public static final String ADMIN_BASE_PATH = "/api/admin";
        public static final String API_KEY_HEADER = "X-API-Key";

        private Api() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    /**
     * Константы, связанные с сообщениями об ошибках.
     */
    public static class Messages {
        public static final String ERROR_RECIPE_NOT_FOUND = "Рецепты не найдены";
        public static final String ERROR_INVALID_INGREDIENTS = "Неверный формат списка ингредиентов";
        public static final String ERROR_INTERNAL_SERVER = "Внутренняя ошибка сервера";
        public static final String ERROR_UNAUTHORIZED = "Неавторизованный доступ";

        private Messages() {
            throw new UnsupportedOperationException("Utility class");
        }
    }
}