package com.example.cookbot.repository;

import com.example.cookbot.model.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с рецептами.
 *
 * @author dev
 * @version 1.0
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByExternalIdAndSourceUrl(
            @Param("externalId") String externalId,
            @Param("sourceUrl") String sourceUrl
    );

    boolean existsByExternalIdAndSourceUrl(
            @Param("externalId") String externalId,
            @Param("sourceUrl") String sourceUrl
    );

    @Query(value = """
            SELECT r.* FROM recipes r
            INNER JOIN recipe_ingredients ri ON r.id = ri.recipe_id
            WHERE ri.ingredient_id IN :ingredientIds
            GROUP BY r.id
            HAVING COUNT(DISTINCT ri.ingredient_id) = :ingredientsCount
            ORDER BY r.likes_count DESC, r.created_at DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Recipe> findByIngredientsExactMatch(
            @Param("ingredientIds") List<Long> ingredientIds,
            @Param("ingredientsCount") int ingredientsCount,
            @Param("limit") int limit
    );

    /**
     * Поиск рецептов с НЕЧЕТКИМ совпадением (>= 50% ингредиентов).
     */
    @Query(value = """
        SELECT r.*, 
               COUNT(DISTINCT ri.ingredient_id) as match_count,
               (COUNT(DISTINCT ri.ingredient_id) * 100.0 / :ingredientsCount) as match_percent
        FROM recipes r
        INNER JOIN recipe_ingredients ri ON r.id = ri.recipe_id
        WHERE ri.ingredient_id IN :ingredientIds
          AND r.id NOT IN :excludeRecipeIds
        GROUP BY r.id
        HAVING COUNT(DISTINCT ri.ingredient_id) >= :minMatchCount
        ORDER BY match_percent DESC, r.likes_count DESC, r.created_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Recipe> findByIngredientsFuzzyMatch(
            @Param("ingredientIds") List<Long> ingredientIds,
            @Param("ingredientsCount") int ingredientsCount,
            @Param("minMatchCount") int minMatchCount,
            @Param("excludeRecipeIds") List<Long> excludeRecipeIds,
            @Param("limit") int limit
    );

    List<Recipe> findTop10ByOrderByLikesCountDescCreatedAtDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.likesCount = r.likesCount + 1 WHERE r.id = :recipeId")
    int incrementLikesCount(@Param("recipeId") Long recipeId);

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.likesCount = CASE WHEN r.likesCount > 0 THEN r.likesCount - 1 ELSE 0 END WHERE r.id = :recipeId")
    int decrementLikesCount(@Param("recipeId") Long recipeId);

    @Query("SELECT r.likesCount FROM Recipe r WHERE r.id = :recipeId")
    Integer findLikesCountById(@Param("recipeId") Long recipeId);

    List<Recipe> findByTitleContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
}