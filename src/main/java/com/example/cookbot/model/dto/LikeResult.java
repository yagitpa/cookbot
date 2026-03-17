package com.example.cookbot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для результата операции постановки/снятия лайка.
 * <p>
 * Возвращается сервисом лайков после успешной операции.
 * </p>
 *
 * @param isLiked   Флаг: установлен ли лайк в текущий момент
 * @param count     Актуальное количество лайков рецепта
 * @param message   Сообщение для пользователя (опционально)
 *
 * @author dev
 * @version 1.0
 * @see com.example.cookbot.service.LikeService
 */
@Schema(description = "Результат операции лайка")
public record LikeResult(

        @Schema(description = "Флаг: установлен ли лайк", example = "true")
        boolean isLiked,

        @Schema(description = "Актуальное количество лайков", example = "15")
        int count,

        @Schema(description = "Сообщение для отображения пользователю", example = "Лайк поставлен!", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String message

) {
    /**
     * Конструктор без сообщения.
     *
     * @param isLiked установлен ли лайк
     * @param count   количество лайков
     */
    public LikeResult(boolean isLiked, int count) {
        this(isLiked, count, isLiked ? "Лайк поставлен!" : "Лайк удален");
    }

    /**
     * Статический фабричный метод для успешного лайка.
     *
     * @param count количество лайков
     * @return экземпляр LikeResult
     */
    public static LikeResult liked(int count) {
        return new LikeResult(true, count, "Рецепт понравился! 👍");
    }

    /**
     * Статический фабричный метод для снятия лайка.
     *
     * @param count количество лайков
     * @return экземпляр LikeResult
     */
    public static LikeResult unliked(int count) {
        return new LikeResult(false, count, "Лайк удален");
    }
}