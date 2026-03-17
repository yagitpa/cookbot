package com.example.cookbot.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для ответа с данными рецепта.
 * <p>
 * Содержит всю информацию о рецепте, необходимую для отображения
 * в Telegram боте или REST API.
 * </p>
 *
 * @param id            Уникальный идентификатор рецепта
 * @param title         Название рецепта
 * @param description   Краткое описание
 * @param instructions  Пошаговая инструкция приготовления
 * @param imageUrl      URL изображения блюда
 * @param ingredients   Список ингредиентов с количеством
 * @param likesCount    Количество лайков
 * @param isLiked       Поставил ли текущий пользователь лайк
 * @param createdAt     Дата создания рецепта
 *
 * @author dev
 * @version 1.0
 * @see com.example.cookbot.model.entity.Recipe
 */
@Schema(description = "Данные рецепта для отображения пользователю")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RecipeResponse(

        @Schema(description = "Уникальный идентификатор рецепта", example = "1")
        Long id,

        @Schema(description = "Название рецепта", example = "Омлет с сыром")
        String title,

        @Schema(description = "Краткое описание рецепта", example = "Классический завтрак за 10 минут")
        String description,

        @Schema(description = "Пошаговая инструкция приготовления", example = "1. Взбить яйца...\n2. Добавить сыр...")
        String instructions,

        @Schema(description = "URL изображения готового блюда", example = "https://example.com/omelet.jpg")
        String imageUrl,

        @Schema(description = "Список ингредиентов с количеством")
        List<IngredientDto> ingredients,

        @Schema(description = "Количество лайков", example = "42")
        Integer likesCount,

        @Schema(description = "Флаг: поставил ли текущий пользователь лайк", example = "false")
        Boolean isLiked,

        @Schema(description = "Дата создания рецепта", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt

) {
    /**
     * Статический фабричный метод для создания ответа без информации о лайке.
     *
     * @param id           идентификатор
     * @param title        название
     * @param description  описание
     * @param instructions инструкция
     * @param imageUrl     URL изображения
     * @param ingredients  ингредиенты
     * @param likesCount   количество лайков
     * @param createdAt    дата создания
     * @return экземпляр RecipeResponse
     */
    public static RecipeResponse of(Long id, String title, String description, String instructions,
                                    String imageUrl, List<IngredientDto> ingredients,
                                    Integer likesCount, LocalDateTime createdAt) {
        return new RecipeResponse(id, title, description, instructions, imageUrl,
                ingredients, likesCount, false, createdAt);
    }
}