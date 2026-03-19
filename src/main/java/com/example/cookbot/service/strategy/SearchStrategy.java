package com.example.cookbot.service.strategy;

import com.example.cookbot.model.entity.Recipe;

import java.util.List;

/**
 * Интерфейс стратегии поиска рецептов.
 * <p>
 * Реализует различные алгоритмы поиска (точный, нечеткий).
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see ExactMatchStrategy
 * @see FuzzyMatchStrategy
 */
public interface SearchStrategy {

    /**
     * Выполняет поиск рецептов по списку ингредиентов.
     *
     * @param ingredientIds ID ингредиентов для поиска
     * @param limit         максимальное количество результатов
     *
     * @return список найденных рецептов
     */
    List<Recipe> search(List<Long> ingredientIds, int limit);

    /**
     * Выполняет поиск рецептов с исключением указанных рецептов.
     *
     * @param ingredientIds    ID ингредиентов для поиска
     * @param excludeRecipeIds ID рецептов для исключения
     * @param limit            максимальное количество результатов
     *
     * @return список найденных рецептов
     */
    List<Recipe> search(List<Long> ingredientIds, List<Long> excludeRecipeIds, int limit);
}