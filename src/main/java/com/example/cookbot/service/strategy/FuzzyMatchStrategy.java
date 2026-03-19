package com.example.cookbot.service.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import com.example.cookbot.model.entity.Recipe;
import com.example.cookbot.repository.RecipeRepository;
import com.example.cookbot.util.Constants;

import java.util.Collections;
import java.util.List;

/**
 * Стратегия нечеткого поиска рецептов.
 * <p>
 * Возвращает рецепты, где совпадает минимум 50% запрошенных ингредиентов. Результаты сортируются по проценту совпадения (убывание).
 * </p>
 *
 * @author dev
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FuzzyMatchStrategy implements SearchStrategy {

    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipe> search(List<Long> ingredientIds, int limit) {
        return search(ingredientIds, Collections.emptyList(), limit);
    }

    @Override
    public List<Recipe> search(List<Long> ingredientIds, List<Long> excludeRecipeIds, int limit) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            log.debug("Пустой список ингредиентов для нечеткого поиска");
            return Collections.emptyList();
        }

        // Вычисляем минимальное количество совпадений (50%)
        int minMatchCount = (int) Math.ceil(ingredientIds.size() * Constants.Search.MIN_MATCH_PERCENTAGE);

        // Гарантируем минимум 1 совпадение
        minMatchCount = Math.max(1, minMatchCount);

        log.debug("Нечеткий поиск: {} ингредиентов, мин. совпадений: {}, лимит: {}",
                ingredientIds.size(), minMatchCount, limit);

        // Исключаем уже найденные точные совпадения
        List<Long> excludeIds = excludeRecipeIds != null ? excludeRecipeIds : Collections.emptyList();

        // Если список исключений пустой, передаем [0L] для корректной работы SQL
        if (excludeIds.isEmpty()) {
            excludeIds = Collections.singletonList(0L);
        }

        List<Recipe> results = recipeRepository.findByIngredientsFuzzyMatch(
                ingredientIds,
                ingredientIds.size(),
                minMatchCount,
                excludeIds,
                limit
        );

        log.debug("Найдено {} рецептов с нечетким совпадением", results.size());
        return results;
    }
}