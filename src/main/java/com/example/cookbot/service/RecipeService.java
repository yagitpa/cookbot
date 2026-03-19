package com.example.cookbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.cookbot.model.dto.RecipeResponse;
import com.example.cookbot.model.dto.SearchRequest;
import com.example.cookbot.model.entity.Ingredient;
import com.example.cookbot.model.entity.Recipe;
import com.example.cookbot.model.entity.RecipeIngredient;
import com.example.cookbot.repository.IngredientRepository;
import com.example.cookbot.service.strategy.ExactMatchStrategy;
import com.example.cookbot.service.strategy.FuzzyMatchStrategy;
import com.example.cookbot.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для поиска рецептов по ингредиентам.
 * <p>
 * Реализует двухуровневый поиск:
 * <ol>
 *     <li>Точное совпадение (100% ингредиентов)</li>
 *     <li>Нечеткое совпадение (>= 50% ингредиентов)</li>
 * </ol>
 * Результаты кэшируются для повышения производительности.
 * </p>
 *
 * @author dev
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final IngredientRepository ingredientRepository;
    private final ExactMatchStrategy exactStrategy;
    private final FuzzyMatchStrategy fuzzyStrategy;

    /**
     * Поиск рецептов по ингредиентам.
     * <p>
     * Метод кэшируется по хэшу нормализованных ингредиентов.
     * </p>
     *
     * @param request запрос на поиск
     *
     * @return список найденных рецептов
     */
    @Cacheable(value = Constants.Cache.CACHE_RECIPES, key = "#request.normalizedIngredients.hashCode()")
    @Transactional(readOnly = true)
    public List<RecipeResponse> findRecipes(SearchRequest request) {
        log.info("Поиск рецептов для: {}", request.rawIngredients());

        // 1. Находим ID ингредиентов в БД
        List<Ingredient> ingredients = ingredientRepository.findByNameIgnoreCaseIn(
                request.normalizedIngredients()
        );

        if (ingredients.isEmpty()) {
            log.warn("Ингредиенты не найдены в базе данных");
            return List.of();
        }

        List<Long> ingredientIds = ingredients.stream()
                .map(Ingredient::getId)
                .toList();

        log.debug("Найдено {} ингредиентов в БД", ingredientIds.size());

        // 2. Точный поиск (100% совпадение)
        List<Recipe> exactMatches = exactStrategy.search(
                ingredientIds,
                Constants.Search.MAX_RECIPES_LIMIT
        );

        log.debug("Точные совпадения: {}", exactMatches.size());

        // 3. Нечеткий поиск (если точных < 10)
        List<Recipe> results = new ArrayList<>(exactMatches);

        if (results.size() < Constants.Search.MAX_RECIPES_LIMIT) {
            int remaining = Constants.Search.MAX_RECIPES_LIMIT - results.size();

            List<Long> excludeIds = exactMatches.stream()
                    .map(Recipe::getId)
                    .toList();

            List<Recipe> fuzzyMatches = fuzzyStrategy.search(
                    ingredientIds,
                    excludeIds,
                    remaining
            );

            results.addAll(fuzzyMatches);
            log.debug("Нечеткие совпадения: {}", fuzzyMatches.size());
        }

        // 4. Ограничиваем результат до 10 рецептов
        List<Recipe> limitedResults = results.stream()
                .limit(Constants.Search.MAX_RECIPES_LIMIT)
                .toList();

        log.info("Всего найдено {} рецептов", limitedResults.size());

        // 5. Конвертируем в DTO
        return limitedResults.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Поиск популярных рецептов.
     *
     * @return топ-10 популярных рецептов
     */
    @Cacheable(value = Constants.Cache.CACHE_POPULAR, key = "'top'")
    @Transactional(readOnly = true)
    public List<RecipeResponse> findPopularRecipes() {
        log.info("Получение популярных рецептов");
        // Реализация будет добавлена в следующем этапе
        return List.of();
    }

    /**
     * Конвертирует сущность Recipe в DTO RecipeResponse.
     *
     * @param recipe сущность рецепта
     *
     * @return DTO ответа
     */
    private RecipeResponse toResponse(Recipe recipe) {
        List<Ingredient> ingredients = recipe.getRecipeIngredients().stream()
                .map(RecipeIngredient::getIngredient)
                .toList();

        List<com.example.cookbot.model.dto.IngredientDto> ingredientDtos = ingredients.stream()
                .map(ing -> com.example.cookbot.model.dto.IngredientDto.of(
                        ing.getName(),
                        recipe.getRecipeIngredients().stream()
                                .filter(ri -> ri.getIngredient().getId().equals(ing.getId()))
                                .findFirst()
                                .map(RecipeIngredient::getQuantity)
                                .orElse(null)
                ))
                .toList();

        return RecipeResponse.of(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                ingredientDtos,
                recipe.getLikesCount(),
                recipe.getCreatedAt()
        );
    }

    /**
     * Конвертирует список сущностей в список DTO.
     *
     * @param recipes список сущностей
     * @param userId  ID пользователя для определения лайков
     *
     * @return список DTO
     */
    public List<RecipeResponse> toResponseList(List<Recipe> recipes, Long userId) {
        return recipes.stream()
                .map(recipe -> toResponse(recipe, userId))
                .toList();
    }

    /**
     * Конвертирует сущность в DTO с информацией о лайке пользователя.
     *
     * @param recipe сущность рецепта
     * @param userId ID пользователя
     *
     * @return DTO ответа
     */
    private RecipeResponse toResponse(Recipe recipe, Long userId) {
        RecipeResponse response = toResponse(recipe);
        // Информация о лайке будет добавлена через LikeService
        return response;
    }
}