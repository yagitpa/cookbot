package com.example.cookbot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO для ингредиента при импорте рецепта.
 *
 * @param name     Название ингредиента
 * @param quantity Количество
 *
 * @author dev
 * @version 1.0
 */
@Schema(description = "Ингредиент для импорта")
public record ImportIngredientDto(

        @Schema(description = "Название ингредиента", example = "свекла", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Название ингредиента обязательно")
        String name,

        @Schema(description = "Количество", example = "2 шт")
        String quantity

) {
}