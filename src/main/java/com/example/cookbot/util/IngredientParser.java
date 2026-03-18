package com.example.cookbot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.cookbot.model.dto.SearchRequest;

import java.util.List;

/**
 * Специализированный парсер для обработки пользовательского ввода ингредиентов.
 * <p>
 * Инкапсулирует логику преобразования сырого ввода в структурированный запрос. Поддерживает как простой список, так и естественный язык.
 * </p>
 *
 * @author dev
 * @version 1.0
 * @see SearchRequest
 * @see StringUtils
 * @see NaturalLanguageParser
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IngredientParser {

    /**
     * Парсит сырой ввод пользователя в SearchRequest.
     *
     * @param rawInput сырой ввод от пользователя
     *
     * @return SearchRequest с нормализованными данными
     *
     * @throws IllegalArgumentException если ввод пустой или невалидный
     */
    public static SearchRequest parse(String rawInput) {
        log.debug("Парсинг ввода ингредиентов: {}", rawInput);

        if (StringUtils.isBlank(rawInput)) {
            log.warn("Пустой ввод ингредиентов");
            throw new IllegalArgumentException("Список ингредиентов не может быть пустым");
        }

        // Определяем тип ввода
        NaturalLanguageParser.InputType inputType = NaturalLanguageParser.detectInputType(rawInput);
        log.debug("Тип ввода: {}", inputType);

        List<String> normalized;
        if (inputType == NaturalLanguageParser.InputType.NATURAL_LANGUAGE) {
            // Естественный язык: "что приготовить из курицы"
            normalized = NaturalLanguageParser.extractIngredients(rawInput);
        } else {
            // Простой список: "курица, яблоки"
            normalized = StringUtils.parseIngredients(rawInput);
        }

        if (normalized.isEmpty()) {
            log.warn("После нормализации список ингредиентов пуст");
            throw new IllegalArgumentException("Не найдено валидных ингредиентов");
        }

        log.debug("Нормализованные ингредиенты: {}", normalized);

        return new SearchRequest(rawInput, normalized);
    }

    /**
     * Парсит сырой ввод с кастомным лимитом.
     *
     * @param rawInput сырой ввод
     * @param limit    максимальное количество рецептов
     *
     * @return SearchRequest
     */
    public static SearchRequest parse(String rawInput, int limit) {
        SearchRequest request = parse(rawInput);
        return new SearchRequest(
                request.rawIngredients(),
                request.normalizedIngredients(),
                Math.min(limit, Constants.Search.MAX_ALLOWED_LIMIT),
                request.includeFuzzy()
        );
    }

    /**
     * Валидирует список ингредиентов на допустимость.
     *
     * @param ingredients список ингредиентов
     *
     * @return true если список валиден
     */
    public static boolean isValid(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return false;
        }

        return ingredients.stream()
                .allMatch(ing -> ing != null &&
                        ing.length() >= Constants.Search.MIN_INGREDIENT_LENGTH &&
                        ing.length() <= Constants.Search.MAX_INGREDIENT_LENGTH);
    }

    /**
     * Извлекает ингредиенты из текста сообщения после команды.
     *
     * @param message полное сообщение от пользователя
     * @param command команда (например, "/find")
     *
     * @return строка с ингредиентами (без команды)
     */
    public static String extractIngredientsFromCommand(String message, String command) {
        if (StringUtils.isBlank(message) || StringUtils.isBlank(command)) {
            return "";
        }

        return message.replaceFirst(command, "")
                .trim()
                .replaceAll("^[:\\s]+", "");
    }
}