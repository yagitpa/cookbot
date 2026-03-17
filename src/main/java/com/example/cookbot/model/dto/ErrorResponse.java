package com.example.cookbot.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для стандартизированного ответа при ошибках API.
 * <p>
 * Используется глобальным обработчиком исключений.
 * </p>
 *
 * @param timestamp   Время возникновения ошибки
 * @param status      HTTP статус
 * @param error       Тип ошибки
 * @param message     Сообщение об ошибке
 * @param path        Путь запроса
 * @param details     Детали валидации (опционально)
 *
 * @author dev
 * @version 1.0
 * @see com.example.cookbot.exception.GlobalExceptionHandler
 */
@Schema(description = "Стандартизированный ответ при ошибке")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

        @Schema(description = "Время возникновения ошибки", example = "2024-01-15T10:30:00")
        LocalDateTime timestamp,

        @Schema(description = "HTTP статус", example = "400")
        Integer status,

        @Schema(description = "Тип ошибки", example = "Bad Request")
        String error,

        @Schema(description = "Сообщение об ошибке", example = "Неверные данные запроса")
        String message,

        @Schema(description = "Путь запроса", example = "/api/v1/recipes/search")
        String path,

        @Schema(description = "Детали ошибок валидации")
        List<FieldErrorDto> details

) {
    /**
     * DTO для детали ошибки валидации поля.
     */
    @Schema(description = "Деталь ошибки валидации поля")
    public record FieldErrorDto(

            @Schema(description = "Имя поля", example = "rawIngredients")
            String field,

            @Schema(description = "Сообщение об ошибке", example = "Не может быть пустым")
            String message

    ) {
    }
}