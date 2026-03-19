package com.example.cookbot.service.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import com.example.cookbot.model.entity.Recipe;
import com.example.cookbot.repository.RecipeRepository;

import java.util.Collections;
import java.util.List;

/**
 * Стратегия точного поиска рецептов.
 * <p>
 * Возвращает рецепты, которые содержат ВСЕ указанные ингредиенты.
 * Дополнительные ингредиенты в рецепте допускаются.
 * </p>
 *
 * @author dev
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExactMatchStrategy implements SearchStrategy {

    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipe> search(List<Long> ingredientIds, int limit) {
        return search(ingredientIds, Collections.emptyList(), limit);
    }

    @Override
    public List<Recipe> search(List<Long> ingredientIds, List<Long> excludeRecipeIds, int limit) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            log.debug("Пустой список ингредиентов для точного поиска");
            return Collections.emptyList();
        }

        log.debug("Точный поиск по {} ингредиентам, лимит: {}", ingredientIds.size(), limit);

        // Для точного поиска исключаем рецепты только если явно переданы
        List<Recipe> results;
        if (excludeRecipeIds == null || excludeRecipeIds.isEmpty()) {
            results = recipeRepository.findByIngredientsExactMatch(ingredientIds, ingredientIds.size(), limit);
        } else {
            // Точный поиск не поддерживает исключение напрямую, фильтруем в памяти
            results = recipeRepository.findByIngredientsExactMatch(ingredientIds, ingredientIds.size(), limit * 2);
            results = results.stream()
                    .filter(recipe -> !excludeRecipeIds.contains(recipe.getId()))
                    .limit(limit)
                    .toList();
        }

        log.debug("Найдено {} рецептов с точным совпадением", results.size());
        return results;
    }
}