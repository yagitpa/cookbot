package com.example.cookbot.repository;

import com.example.cookbot.model.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с ингредиентами.
 *
 * @author dev
 * @version 1.0
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Находит ингредиент по нормализованному названию (регистронезависимо).
     *
     * @param name нормализованное название ингредиента
     *
     * @return Optional с ингредиентом, если найден
     */
    Optional<Ingredient> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Находит все ингредиенты по списку названий.
     * <p>
     * Используется для поиска рецептов по множеству ингредиентов.
     *
     * @param names список названий ингредиентов
     *
     * @return список найденных ингредиентов
     */
    List<Ingredient> findByNameIgnoreCaseIn(@Param("names") List<String> names);

    /**
     * Проверяет существование ингредиента по названию.
     *
     * @param name название ингредиента
     *
     * @return true если ингредиент существует
     */
    boolean existsByNameIgnoreCase(@Param("name") String name);

    /**
     * Поиск ингредиентов по частичному совпадению названия (для автодополнения).
     *
     * @param prefix начало названия ингредиента
     * @param limit  максимальное количество результатов
     *
     * @return список подходящих ингредиентов
     */
    @Query("SELECT i FROM Ingredient i WHERE LOWER(i.name) LIKE LOWER(CONCAT(:prefix, '%')) ORDER BY i.name LIMIT :limit")
    List<Ingredient> findByNameStartingWithIgnoreCase(@Param("prefix") String prefix, @Param("limit") int limit);
}