package com.example.cookbot.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO для массового импорта рецептов из внешних источников.
 * <p>
 * Используется административным API для загрузки рецептов.
 * </p>
 *
 * @param title       Название рецепта
 * @param description Описание
 * @param instructions Инструкция приготовления
 * @param imageUrl    URL изображения
 * @param sourceUrl   URL источника
 * @param externalId  Внешний идентификатор (для предотвращения дублей)
 * @param ingredients Список ингредиентов
 *
 * @author dev
 * @version 1.0
 * @see com.example.cookbot.importer.service.ImportService
 */
@Schema(description = "Данные рецепта для импорта")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImportRecipeDto(

        @Schema(description = "Название рецепта", example = "Борщ", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Название рецепта обязательно")
        String title,

        @Schema(description = "Описание рецепта", example = "Классический украинский борщ")
        @NotBlank(message = "Описание рецепта обязательно")
        String description,

        @Schema(description = "Инструкция по приготовлению", example = "1. Нарезать овощи...\n2. Сварить бульон...")
        @NotBlank(message = "Инструкция обязательна")
        String instructions,

        @Schema(description = "URL изображения", example = "https://example.com/borscht.jpg")
        String imageUrl,

        @Schema(description = "URL источника рецепта", example = "https://cooksite.ru/recipe/123")
        String sourceUrl,

        @Schema(description = "Внешний идентификатор для предотвращения дублей", example = "123")
        String externalId,

        @Schema(description = "Список ингредиентов")
        @Size(min = 1, message = "Рецепт должен содержать хотя бы один ингредиент")
        List<ImportIngredientDto> ingredients

) {
}