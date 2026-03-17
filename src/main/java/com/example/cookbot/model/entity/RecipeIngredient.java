package com.example.cookbot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность связи рецепта и ингредиента.
 * <p>
 * Представляет многие-ко-многим связь между рецептами и ингредиентами
 * с дополнительным полем количества.
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see Recipe
 * @see Ingredient
 */
@Entity
@Table(name = "recipe_ingredients",
        uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredient {

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Ссылка на рецепт.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false, foreignKey = @ForeignKey(name = "fk_recipe_ingredients_recipe"))
    private Recipe recipe;

    /**
     * Ссылка на ингредиент.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false, foreignKey = @ForeignKey(name = "fk_recipe_ingredients_ingredient"))
    private Ingredient ingredient;

    /**
     * Количество ингредиента.
     * <p>
     * Пример: "200 г", "3 шт", "2 ст.л"
     * </p>
     */
    @Column(name = "quantity", length = 100)
    private String quantity;

    /**
     * Дата создания записи.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Равенство по ID для корректной работы в коллекциях.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredient that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}