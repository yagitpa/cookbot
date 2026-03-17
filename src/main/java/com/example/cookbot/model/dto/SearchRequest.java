package com.example.cookbot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO для запроса поиска рецептов по ингредиентам.
 * <p>
 * Используется в REST API и Telegram боте для передачи параметров поиска.
 * </p>
 *
 * @param rawIngredients       Сырая строка ингредиентов от пользователя
 * @param normalizedIngredients Нормализованный список ингредиентов (lowercase, trimmed)
 * @param limit                Максимальное количество рецептов для возврата
 * @param includeFuzzy         Включать ли нечеткие совпадения (>= 50%)
 *
 * @author dev
 * @version 1.0
 * @see com.example.cookbot.service.RecipeService
 */
@Schema(description = "Запрос на поиск рецептов по ингредиентам")
public record SearchRequest(

        @Schema(description = "Сырая строка ингредиентов, перечисленных через запятую",
                example = "яйца, мука, молоко", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Список ингредиентов не может быть пустым")
        String rawIngredients,

        @Schema(description = "Нормализованный список ингредиентов",
                example = "[\"яйца\", \"мука\", \"молоко\"]", hidden = true)
        List<String> normalizedIngredients,

        @Schema(description = "Максимальное количество рецептов для возврата",
                example = "10", defaultValue = "10")
        @Size(max = 50, message = "Лимит не может превышать 50 рецептов")
        Integer limit,

        @Schema(description = "Включать ли нечеткие совпадения (минимум 50% ингредиентов)",
                example = "true", defaultValue = "true")
        Boolean includeFuzzy

) {
    /**
     * Конструктор по умолчанию с значениями по умолчанию.
     *
     * @param rawIngredients сырая строка ингредиентов
     */
    public SearchRequest(String rawIngredients) {
        this(rawIngredients, null, 10, true);
    }

    /**
     * Конструктор с нормализованными ингредиентами.
     *
     * @param rawIngredients        сырая строка
     * @param normalizedIngredients обработанный список
     */
    public SearchRequest(String rawIngredients, List<String> normalizedIngredients) {
        this(rawIngredients, normalizedIngredients, 10, true);
    }
}