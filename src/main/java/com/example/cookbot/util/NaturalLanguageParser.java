package com.example.cookbot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Парсер для обработки естественного языка пользователя.
 *
 * @author dev
 * @version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NaturalLanguageParser {

    /**
     * Стоп-слова русского языка. Используем HashSet для избежания дубликатов.
     */
    private static final Set<String> STOP_WORDS = new HashSet<>(List.of(
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
            // Частицы (убран дубликат "ли")
            "бы", "вот", "это", "то", "всё", "все", "весь", "ли",
            // Кулинарные общие слова
            "готовить", "готовлю", "готовим", "сварить", "жарить", "печь",
            "запечь", "тушить", "порезать", "нарезать", "добавить", "взять",
            "нужно", "надо", "требуется", "понадобится", "понравится"
    ));

    /**
     * Ключевые слова для определения запроса рецепта.
     */
    private static final List<String> SEARCH_KEYWORDS = List.of(
            "приготовить", "рецепт", "рецепты", "блюда", "блюдо", "сделать",
            "ужин", "обед", "завтрак", "перекус", "салат", "суп", "борщ",
            "каша", "пирог", "торт", "десерт", "запеканка", "котлеты",
            "из чего", "что можно", "как сделать", "как приготовить", "что приготовить"
    );

    public static String removeStopWords(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> !STOP_WORDS.contains(word))
                .filter(word -> word.length() > Constants.Search.MIN_INGREDIENT_LENGTH)
                .collect(Collectors.joining(" "));
    }

    public static List<String> extractIngredients(String text) {
        log.debug("Обработка естественного языка: {}", text);
        String cleaned = removeStopWords(text);
        log.debug("После удаления стоп-слов: {}", cleaned);
        List<String> ingredients = StringUtils.parseIngredients(cleaned);
        log.debug("Извлеченные ингредиенты: {}", ingredients);
        return ingredients;
    }

    public static String normalizeEnding(String word) {
        if (StringUtils.isBlank(word)) {
            return word;
        }

        String result = word.toLowerCase();
        result = result.replaceAll("(аниями|ениями|ами|ями)$", "");
        result = result.replaceAll("(анием|ением|ами|ями)$", "");
        result = result.replaceAll("(ания|ения|ах|ях|ов|ев|ей)$", "");
        result = result.replaceAll("(ыми|ими|ого|его|ому|ему)$", "");
        result = result.replaceAll("(ым|им|ой|ей)$", "");
        result = result.replaceAll("(ах|ях|ом|ем|ём)$", "");
        result = result.replaceAll("(ы|и|у|ю|а|я|о|е|ё)$", "");
        result = result.replaceAll("ь$", "");

        return result;
    }

    public static boolean isRecipeSearchQuery(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        String lower = text.toLowerCase().trim();
        for (String keyword : SEARCH_KEYWORDS) {
            if (lower.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public enum InputType {
        SIMPLE_LIST,
        NATURAL_LANGUAGE,
        UNKNOWN
    }

    public static InputType detectInputType(String text) {
        if (StringUtils.isBlank(text)) {
            return InputType.UNKNOWN;
        }

        if (text.contains(Constants.Search.INGREDIENT_SEPARATOR)) {
            return InputType.SIMPLE_LIST;
        }

        if (isRecipeSearchQuery(text)) {
            return InputType.NATURAL_LANGUAGE;
        }

        return InputType.UNKNOWN;
    }
}