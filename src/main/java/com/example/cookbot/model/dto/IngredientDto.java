package com.example.cookbot.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для представления ингредиента в рецепте.
 * <p>
 * Содержит название ингредиента и его количество в конкретном рецепте.
 * </p>
 *
 * @param name     Название ингредиента
 * @param quantity Количество (например, "200 г", "3 шт")
 *
 * @author dev
 * @version 1.0
 * @see RecipeResponse
 */
@Schema(description = "Ингредиент рецепта с количеством")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IngredientDto(

        @Schema(description = "Название ингредиента", example = "яйца")
        String name,

        @Schema(description = "Количество ингредиента", example = "3 шт")
        String quantity

) {
    /**
     * Статический фабричный метод для создания DTO.
     *
     * @param name     название ингредиента
     * @param quantity количество
     * @return экземпляр IngredientDto
     */
    public static IngredientDto of(String name, String quantity) {
        return new IngredientDto(name, quantity);
    }

    /**
     * Статический фабричный метод для создания DTO без количества.
     *
     * @param name название ингредиента
     * @return экземпляр IngredientDto
     */
    public static IngredientDto of(String name) {
        return new IngredientDto(name, null);
    }
}