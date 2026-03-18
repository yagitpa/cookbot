package com.example.cookbot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Парсер для обработки естественного языка пользователя.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NaturalLanguageParser {

    /**
     * Стоп-слова русского языка.
     */
    private static final Set<String> STOP_WORDS = Set.of(
            // Предлоги
            "из", "с", "со", "в", "во", "на", "над", "под", "при", "через",
            "для", "без", "кроме", "после", "перед", "между", "около",
            // Союзы
            "и", "или", "а", "но", "да", "же", "что", "чтобы",
            // Общие слова
            "как", "можно", "нельзя", "хочу", "хотел", "приготовить",
            "сделать", "блюда", "блюдо", "рецепт", "рецепты", "еда", "кушать",
            "есть", "будет", "будут", "был", "была", "было", "были",
            // Вопросы
            "какие", "какой", "какая", "какое", "каких", "каким", "какими",
            // Частицы
            "бы", "вот", "это", "то", "всё", "все", "весь",
            // Кулинарные общие слова
            "готовить", "готовлю", "готовим", "сварить", "жарить", "печь",
            "запечь", "тушить", "порезать", "нарезать", "добавить", "взять",
            "нужно", "надо", "требуется", "понадобится", "понравится"
    );

    /**
     * Ключевые слова, указывающие на запрос рецепта.
     * <p>
     * Используем полные слова для надежного совпадения.
     * </p>
     */
    private static final List<String> SEARCH_KEYWORDS = List.of(
            "приготовить", "рецепт", "рецепты", "блюда", "блюдо", "сделать",
            "ужин", "обед", "завтрак", "перекус", "салат", "суп", "борщ",
            "каша", "пирог", "торт", "десерт", "запеканка", "котлеты",
            "из чего", "что можно", "как сделать", "как приготовить", "что приготовить"
    );

    /**
     * Очищает текст от стоп-слов.
     */
    public static String removeStopWords(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> !STOP_WORDS.contains(word))
                .filter(word -> word.length() > Constants.Search.MIN_INGREDIENT_LENGTH)
                .collect(Collectors.joining(" "));
    }

    /**
     * Извлекает ингредиенты из текста естественного языка.
     */
    public static List<String> extractIngredients(String text) {
        log.debug("Обработка естественного языка: {}", text);
        String cleaned = removeStopWords(text);
        log.debug("После удаления стоп-слов: {}", cleaned);
        List<String> ingredients = StringUtils.parseIngredients(cleaned);
        log.debug("Извлеченные ингредиенты: {}", ingredients);
        return ingredients;
    }

    /**
     * Нормализация окончаний (расширенная версия).
     */
    public static String normalizeEnding(String word) {
        if (StringUtils.isBlank(word)) {
            return word;
        }

        String result = word.toLowerCase();

        // Удаляем окончания в порядке от длинных к коротким
        result = result.replaceAll("(аниями|ениями|ами|ями)$", "");
        result = result.replaceAll("(анием|ением|ами|ями)$", "");
        result = result.replaceAll("(ания|ения|ах|ях|ов|ев|ей)$", "");
        result = result.replaceAll("(ыми|ими|ого|его|ому|ему)$", "");
        result = result.replaceAll("(ым|им|ой|ей|ой|ой)$", "");
        result = result.replaceAll("(ах|ях|ом|ем|ём)$", "");  // <-- Добавлено: -ом, -ем, -ём
        result = result.replaceAll("(ы|и|у|ю|а|я|о|е|ё)$", "");
        result = result.replaceAll("ь$", "");

        return result;
    }

    /**
     * Проверяет, является ли текст запросом на поиск рецепта.
     */
    public static boolean isRecipeSearchQuery(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        String lower = text.toLowerCase().trim();

        // Прямая проверка по ключевым фразам
        for (String keyword : SEARCH_KEYWORDS) {
            if (lower.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Тип ввода пользователя.
     */
    public enum InputType {
        SIMPLE_LIST,
        NATURAL_LANGUAGE,
        UNKNOWN
    }

    /**
     * Определяет тип ввода.
     */
    public static InputType detectInputType(String text) {
        if (StringUtils.isBlank(text)) {
            return InputType.UNKNOWN;
        }

        // Если есть запятые - простой список
        if (text.contains(Constants.Search.INGREDIENT_SEPARATOR)) {
            return InputType.SIMPLE_LIST;
        }

        // Если содержит ключевые слова запроса - естественный язык
        if (isRecipeSearchQuery(text)) {
            return InputType.NATURAL_LANGUAGE;
        }

        return InputType.UNKNOWN;
    }
}