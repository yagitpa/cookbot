package com.example.cookbot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность ингредиента.
 * <p>
 * Представляет уникальный ингредиент в системе. Используется для поиска рецептов по наличию ингредиентов.
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see RecipeIngredient
 */
@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    /**
     * Уникальный идентификатор ингредиента.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Название ингредиента (уникальное).
     * <p>Пример: "яйца", "соль", "перец"
     * </p>
     */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Дата создания записи.
     */
    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Рецепты, в которых используется ингредиент.
     * Связь с рецептами (через таблицу recipe_ingredients)
     */
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private List<RecipeIngredient> recipeIngredients;


    /**
     * Нормализует название ингредиента для поиска.
     * <p>
     *     Приводит к нижнему регистру и удаляет пробелы перед/после названия. Также убирает двойные пробельники внутри строки.
     *     Например: "Яйца" -> "яйца"
     */
    public static String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim().toLowerCase();
    }
}
