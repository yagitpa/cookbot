package com.example.cookbot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилитный класс для работы со строками.
 *
 * @author dev
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils {

    /**
     * Нормализует строку для поиска.
     *
     * @param str исходная строка
     *
     * @return нормализованная строка, или null если вход null/пустой
     */
    public static String normalize(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }
        return str.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    /**
     * Парсит строку ингредиентов в список.
     *
     * @param ingredientsStr строка с ингредиентами
     *
     * @return список нормализованных ингредиентов
     */
    public static List<String> parseIngredients(String ingredientsStr) {
        if (ingredientsStr == null || ingredientsStr.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(ingredientsStr.split(Constants.Search.INGREDIENT_SEPARATOR))
                .map(StringUtils::normalize)
                .filter(s -> s != null && !s.isEmpty())
                .filter(s -> s.length() >= Constants.Search.MIN_INGREDIENT_LENGTH)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, является ли строка null или пустой.
     *
     * @param str проверяемая строка
     *
     * @return true если строка null или пустая
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * Проверяет, является ли строка не null и не пустой.
     *
     * @param str проверяемая строка
     *
     * @return true если строка содержит данные
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Усекает строку до максимальной длины.
     *
     * @param str       исходная строка
     * @param maxLength максимальная длина
     *
     * @return усеченная строка
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Объединяет список строк в одну с разделителем.
     *
     * @param list      список строк
     * @param delimiter разделитель
     *
     * @return объединенная строка
     */
    public static String join(List<String> list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(delimiter, list);
    }

    /**
     * Генерирует хэш для списка строк.
     *
     * @param list список строк
     *
     * @return хэш код
     */
    public static int generateListHash(List<String> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.hashCode();
    }
}