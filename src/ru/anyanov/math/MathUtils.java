package ru.anyanov.math;

// Статический импорт для коротких имен
import static java.lang.Integer.parseInt;
import static java.lang.Math.pow;

public class MathUtils {

    public static double sum(Number... numbers) {
        double total = 0.0;
        for (Number num : numbers) {
            total += num.doubleValue();
        }
        return total;
    }

    public static double power(String xStr, String yStr) {
        int x = parseInt(xStr);  // Короткое имя благодаря static import
        int y = parseInt(yStr);
        return pow(x, y);        // Короткое имя благодаря static import
    }
}