package com.example.cookbot.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность рецепта.
 * <p>
 * Содержит полную информацию о блюде: название, описание,
 * инструкцию по приготовлению, изображение и список ингредиентов.
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see RecipeIngredient
 * @see UserLike
 */
@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    /**
     * Уникальный идентификатор рецепта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Название рецепта.
     */
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    /**
     * Краткое описание рецепта.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Пошаговая инструкция приготовления.
     */
    @Column(name = "instructions", nullable = false, columnDefinition = "TEXT")
    private String instructions;

    /**
     * URL изображения готового блюда.
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * URL источника рецепта (для импорта).
     */
    @Column(name = "source_url", length = 255)
    private String sourceUrl;

    /**
     * Внешний идентификатор рецепта (для предотвращения дублей при импорте).
     */
    @Column(name = "external_id", length = 100)
    private String externalId;

    /**
     * Счетчик лайков (денормализованное поле для быстрого чтения).
     */
    @Column(name = "likes_count", nullable = false)
    @Builder.Default
    private Integer likesCount = 0;

    /**
     * Версия для оптимистичной блокировки.
     * <p>
     * Используется для предотвращения race conditions при обновлении счетчика лайков.
     * </p>
     */
    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 0;

    /**
     * Дата создания рецепта.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего обновления рецепта.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Связь с ингредиентами (через таблицу recipe_ingredients).
     * <p>
     * Cascade ALL позволяет сохранять ингредиенты вместе с рецептом.
     * </p>
     */
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    /**
     * Связь с лайками пользователей.
     */
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserLike> userLikes = new ArrayList<>();

    /**
     * Добавляет ингредиент к рецепту.
     * <p>
     * Двусторонняя связь: устанавливает рецепт в RecipeIngredient.
     * </p>
     *
     * @param ingredient ингредиент
     * @param quantity   количество
     */
    public void addIngredient(Ingredient ingredient, String quantity) {
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .recipe(this)
                .ingredient(ingredient)
                .quantity(quantity)
                .build();
        this.recipeIngredients.add(recipeIngredient);
    }

    /**
     * Удаляет ингредиент из рецепта.
     *
     * @param ingredient ингредиент для удаления
     */
    public void removeIngredient(Ingredient ingredient) {
        this.recipeIngredients.removeIf(ri -> ri.getIngredient().equals(ingredient));
    }

    /**
     * Увеличивает счетчик лайков на 1.
     */
    public void incrementLikes() {
        this.likesCount = this.likesCount + 1;
    }

    /**
     * Уменьшает счетчик лайков на 1.
     */
    public void decrementLikes() {
        this.likesCount = Math.max(0, this.likesCount - 1);
    }
}