package com.example.cookbot.repository;

import com.example.cookbot.model.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с лайками пользователей.
 *
 * @author dev
 * @version 1.0
 */
@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Long> {

    /**
     * Находит лайк пользователя на конкретный рецепт.
     *
     * @param telegramUserId ID пользователя Telegram
     * @param recipeId       ID рецепта
     *
     * @return Optional с лайком, если найден
     */
    Optional<UserLike> findByTelegramUserIdAndRecipeId(
            @Param("telegramUserId") Long telegramUserId,
            @Param("recipeId") Long recipeId
    );

    /**
     * Проверяет, лайкнул ли пользователь рецепт.
     *
     * @param telegramUserId ID пользователя Telegram
     * @param recipeId       ID рецепта
     *
     * @return true если лайк существует
     */
    boolean existsByTelegramUserIdAndRecipeId(
            @Param("telegramUserId") Long telegramUserId,
            @Param("recipeId") Long recipeId
    );

    /**
     * Получает все лайки пользователя.
     *
     * @param telegramUserId ID пользователя Telegram
     *
     * @return список лайков
     */
    List<UserLike> findAllByTelegramUserId(@Param("telegramUserId") Long telegramUserId);

    /**
     * Получает всех пользователей, лайкнувших рецепт.
     *
     * @param recipeId ID рецепта
     *
     * @return список записей лайков
     */
    List<UserLike> findAllByRecipeId(@Param("recipeId") Long recipeId);

    /**
     * Удаляет лайк пользователя на рецепт.
     *
     * @param telegramUserId ID пользователя Telegram
     * @param recipeId       ID рецепта
     */
    void deleteByTelegramUserIdAndRecipeId(
            @Param("telegramUserId") Long telegramUserId,
            @Param("recipeId") Long recipeId
    );

    /**
     * Считает количество лайков для рецепта (дублирующая проверка счетчика).
     *
     * @param recipeId ID рецепта
     *
     * @return количество лайков
     */
    @Query("SELECT COUNT(ul) FROM UserLike ul WHERE ul.recipe.id = :recipeId")
    Long countLikesByRecipeId(@Param("recipeId") Long recipeId);

    /**
     * Получает топ рецептов по количеству лайков от конкретного пользователя (для персонализированных рекомендаций).
     *
     * @param telegramUserId ID пользователя
     * @param limit          максимальное количество результатов
     *
     * @return список рецептов
     */
    @Query("""
            SELECT ul.recipe FROM UserLike ul
            WHERE ul.telegramUserId = :telegramUserId
            ORDER BY ul.recipe.likesCount DESC, ul.createdAt DESC
            LIMIT :limit
            """)
    List<com.example.cookbot.model.entity.Recipe> findTopLikedRecipesByUser(
            @Param("telegramUserId") Long telegramUserId,
            @Param("limit") int limit
    );
}