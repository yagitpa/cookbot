package com.example.cookbot.repository;

import com.example.cookbot.model.entity.Ingredient;
import com.example.cookbot.model.entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * Интеграционные тесты для RecipeRepository.
 *
 * @author dev
 * @version 1.0
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private List<Long> chickenAppleIds;

    @BeforeEach
    void setUp() {
        chickenAppleIds = ingredientRepository.findByNameIgnoreCaseIn(List.of("курица", "яблоки"))
                .stream()
                .map(Ingredient::getId)
                .toList();
    }

    @Test
    void findByIngredientsExactMatch_shouldReturnRecipesWithAllIngredients() {
        // Когда: ищем рецепты с курицей И яблоками (100% совпадение)
        List<Recipe> results = recipeRepository.findByIngredientsExactMatch(
                chickenAppleIds,
                chickenAppleIds.size(),
                10
        );

        // Тогда: должны найти рецепты, содержащие оба ингредиента
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(recipe ->
                recipe.getRecipeIngredients().stream()
                        .map(ri -> ri.getIngredient().getName().toLowerCase())
                        .anyMatch(name -> name.contains("куриц")) &&
                recipe.getRecipeIngredients().stream()
                        .map(ri -> ri.getIngredient().getName().toLowerCase())
                        .anyMatch(name -> name.contains("яблок"))
        );
    }

    @Test
    void findByIngredientsFuzzyMatch_shouldReturnRecipesWithPartialMatch() {
        // Когда: ищем рецепты с >= 50% совпадением (1 из 2 ингредиентов)
        // Исправление: передаем [0L] вместо пустого списка для excludeRecipeIds
        List<Recipe> results = recipeRepository.findByIngredientsFuzzyMatch(
                chickenAppleIds,
                chickenAppleIds.size(),
                1, // minMatchCount = 50% от 2
                List.of(0L), // Исправлено: не null, а список с невалидным ID
                10
        );

        // Тогда: должны найти рецепты с курицей ИЛИ яблоками
        assertThat(results).isNotEmpty();
    }

    @Test
    void incrementLikesCount_shouldAtomicallyIncreaseCounter() {
        // Дано: рецепт с определенным количеством лайков
        Long recipeId = 1L;
        Integer initialLikes = recipeRepository.findLikesCountById(recipeId);

        // Когда: увеличиваем счетчик
        int updated = recipeRepository.incrementLikesCount(recipeId);

        // Тогда: одна запись обновлена
        assertThat(updated).isEqualTo(1);

        // И: счетчик увеличен на 1 (запрашиваем из БД, не из кэша сущности)
        Integer updatedLikes = recipeRepository.findLikesCountById(recipeId);
        assertThat(updatedLikes).isEqualTo(initialLikes + 1);
    }
}