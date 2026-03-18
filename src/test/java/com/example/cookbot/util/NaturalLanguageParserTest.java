package com.example.cookbot.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NaturalLanguageParserTest {

    @ParameterizedTest
    @CsvSource({
            "'из курицы', 'куриц'",
            "'что приготовить из курицы с рисом', 'рис'",
            "'блюда с курицей и яблоками', 'яблок'",
            "'хочу сделать салат из помидоров', 'помидор'",
            "'какие есть рецепты с мясом', 'мяс'"
    })
    void testRemoveStopWords(String input, String expectedSubstring) {
        String result = NaturalLanguageParser.removeStopWords(input);
        assertFalse(result.isEmpty(), "Результат не должен быть пустым для: " + input);
        // Проверяем наличие ожидаемой подстроки (с учетом нормализации)
        boolean contains = result.contains(expectedSubstring) ||
                result.contains(expectedSubstring.replaceAll("с$", ""));
        assertTrue(contains, "Результат '" + result + "' должен содержать: " + expectedSubstring);
    }

    @Test
    void testExtractIngredients() {
        List<String> result = NaturalLanguageParser.extractIngredients("из курицы, с яблоками");
        assertFalse(result.isEmpty(), "Список ингредиентов не должен быть пустым");
        boolean hasChicken = result.stream().anyMatch(s -> s.startsWith("куриц"));
        boolean hasApple = result.stream().anyMatch(s -> s.startsWith("яблок"));
        assertTrue(hasChicken || hasApple, "Должен содержать курицу или яблоки, но был: " + result);
    }

    @ParameterizedTest
    @CsvSource({
            "'что приготовить из курицы', true",
            "'рецепты с мясом', true",
            "'как сделать борщ', true",
            "'ужин из рыбы', true",
            "'курица, яблоки', false",
            "'помидоры, сыр', false",
            "'', false",
            "'просто текст', false"
    })
    void testIsRecipeSearchQuery(String input, boolean expected) {
        boolean result = NaturalLanguageParser.isRecipeSearchQuery(input);
        assertEquals(expected, result, "Неверный результат для: '" + input + "'");
    }

    @ParameterizedTest
    @CsvSource({
            "'курица, яблоки', SIMPLE_LIST",
            "'что приготовить из курицы', NATURAL_LANGUAGE",
            "'из чего сделать ужин', NATURAL_LANGUAGE",
            "'', UNKNOWN",
            "'просто слова', UNKNOWN"
    })
    void testDetectInputType(String input, String expectedType) {
        NaturalLanguageParser.InputType result = NaturalLanguageParser.detectInputType(input);
        assertEquals(
                NaturalLanguageParser.InputType.valueOf(expectedType),
                result,
                "Неверный тип для: '" + input + "'"
        );
    }

    @Test
    void testNormalizeEnding() {
        // Тесты с учетом расширенной нормализации
        assertEquals("куриц", NaturalLanguageParser.normalizeEnding("курицы"));
        assertEquals("яблок", NaturalLanguageParser.normalizeEnding("яблок"));
        assertEquals("помидор", NaturalLanguageParser.normalizeEnding("помидорами"));
        assertEquals("сыр", NaturalLanguageParser.normalizeEnding("сыром"));
        assertEquals("рис", NaturalLanguageParser.normalizeEnding("рисом"));
        assertEquals("картофел", NaturalLanguageParser.normalizeEnding("картофелем"));
    }
}