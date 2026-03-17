package com.example.cookbot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность лайка пользователя.
 * <p>
 * Представляет факт лайка рецепта пользователем Telegram.
 * Уникальное ограничение на (telegram_user_id, recipe_id) предотвращает дублирование.
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see Recipe
 */
@Entity
@Table(name = "user_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"telegram_user_id", "recipe_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLike {

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ID пользователя в Telegram.
     */
    @Column(name = "telegram_user_id", nullable = false)
    private Long telegramUserId;

    /**
     * Ссылка на рецепт.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_likes_recipe"))
    private Recipe recipe;

    /**
     * Дата создания лайка.
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
        if (!(o instanceof UserLike userLike)) return false;
        return id != null && id.equals(userLike.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}