package com.example.cookbot.repository;

import com.example.cookbot.model.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для связующей таблицы рецептов и ингредиентов.
 *
 * @author dev
 * @version 1.0
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    /**
     * Находит связь рецепт-ингредиент по обоим ключам.
     *
     * @param recipeId     ID рецепта
     * @param ingredientId ID ингредиента
     *
     * @return Optional со связью, если найдена
     */
    Optional<RecipeIngredient> findByRecipeIdAndIngredientId(
            @Param("recipeId") Long recipeId,
            @Param("ingredientId") Long ingredientId
    );

    /**
     * Получает все связи для конкретного рецепта (с ингредиентами).
     *
     * @param recipeId ID рецепта
     *
     * @return список связей с ингредиентами
     */
    @Query("SELECT ri FROM RecipeIngredient ri JOIN FETCH ri.ingredient WHERE ri.recipe.id = :recipeId")
    List<RecipeIngredient> findAllByRecipeIdWithIngredients(@Param("recipeId") Long recipeId);

    /**
     * Удаляет все связи для рецепта (при удалении рецепта).
     *
     * @param recipeId ID рецепта
     */
    void deleteAllByRecipeId(@Param("recipeId") Long recipeId);

    /**
     * Проверяет, содержит ли рецепт указанный ингредиент.
     *
     * @param recipeId     ID рецепта
     * @param ingredientId ID ингредиента
     *
     * @return true если связь существует
     */
    boolean existsByRecipeIdAndIngredientId(
            @Param("recipeId") Long recipeId,
            @Param("ingredientId") Long ingredientId
    );

    /**
     * Находит все рецепты, содержащие указанный ингредиент.
     *
     * @param ingredientId ID ингредиента
     *
     * @return список рецептов
     */
    @Query("SELECT DISTINCT ri.recipe FROM RecipeIngredient ri WHERE ri.ingredient.id = :ingredientId")
    List<com.example.cookbot.model.entity.Recipe> findRecipesByIngredientId(@Param("ingredientId") Long ingredientId);
}