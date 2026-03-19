package com.example.cookbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.cookbot.model.dto.RecipeResponse;
import com.example.cookbot.model.dto.SearchRequest;
import com.example.cookbot.model.entity.Ingredient;
import com.example.cookbot.repository.IngredientRepository;
import com.example.cookbot.service.strategy.ExactMatchStrategy;
import com.example.cookbot.service.strategy.FuzzyMatchStrategy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для RecipeService.
 * <p>
 * Используют Mockito для мокирования зависимостей. Не требуют подключения к БД.
 * </p>
 *
 * @author dev
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ExactMatchStrategy exactStrategy;

    @Mock
    private FuzzyMatchStrategy fuzzyStrategy;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void findRecipes_shouldReturnEmptyListWhenNoIngredientsFound() {
        // Дано
        SearchRequest request = new SearchRequest(
                "несуществующий ингредиент",
                List.of("несуществующий ингредиент")
        );
        when(ingredientRepository.findByNameIgnoreCaseIn(any())).thenReturn(List.of());

        // Когда
        List<RecipeResponse> result = recipeService.findRecipes(request);

        // Тогда
        assertThat(result).isEmpty();
    }

    @Test
    void findRecipes_shouldReturnRecipesWhenIngredientsFound() {
        // Дано
        SearchRequest request = new SearchRequest(
                "курица, яблоки",
                List.of("курица", "яблоки")
        );

        // Используем builder для создания сущностей
        Ingredient ingredient1 = Ingredient.builder()
                .id(1L)
                .name("курица")
                .build();
        Ingredient ingredient2 = Ingredient.builder()
                .id(2L)
                .name("яблоки")
                .build();

        when(ingredientRepository.findByNameIgnoreCaseIn(any())).thenReturn(
                List.of(ingredient1, ingredient2)
        );
        when(exactStrategy.search(any(), anyInt())).thenReturn(List.of());
        when(fuzzyStrategy.search(any(), any(), anyInt())).thenReturn(List.of());

        // Когда
        List<RecipeResponse> result = recipeService.findRecipes(request);

        // Тогда
        assertThat(result).isNotNull();
        assertThat(result).isEmpty(); // Так как стратегии возвращают пустые списки
    }
}