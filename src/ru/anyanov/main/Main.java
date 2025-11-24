package ru.anyanov.main;

import ru.anyanov.fraction.Fraction;
import ru.anyanov.city.City;
import ru.anyanov.city.Route;
import ru.anyanov.geometry.Point;
import ru.anyanov.math.MathUtils;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, City> cities = new HashMap<>();

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ ВВОДА ==========

    private static boolean containsDigits(String str) {
        return str != null && str.matches(".*\\d.*");
    }

    private static String getValidStringInput(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if (allowEmpty) {
                    return input;
                } else {
                    System.out.println("Ошибка! Поле не может быть пустым. Попробуйте снова.");
                }
            } else if (containsDigits(input)) {
                System.out.println("Ошибка! Поле не должно содержать цифр. Попробуйте снова.");
            } else {
                return input;
            }
        }
    }

    private static int getValidIntInput(int min, int max, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Ошибка! Число должно быть от " + min + " до " + max + ". Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    private static int getAnyIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    private static double getAnyDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double input = scanner.nextDouble();
                scanner.nextLine();
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите число.");
                scanner.nextLine();
            }
        }
    }

    private static void waitForEnter() {
        System.out.print("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С ГОРОДАМИ ==========

    private static void createCity() {
        System.out.println("\n--- Создание нового города ---");
        String name = getValidStringInput("Введите название города: ", false);

        if (cities.containsKey(name)) {
            System.out.println("Город с таким названием уже существует!");
            return;
        }

        City city = new City(name);
        cities.put(name, city);
        System.out.println("Город " + name + " успешно создан!");
    }

    private static void addRoad() {
        if (cities.size() < 2) {
            System.out.println("Для добавления дороги нужно как минимум 2 города!");
            return;
        }

        System.out.println("\n--- Добавление дороги между городами ---");
        System.out.println("Доступные города: " + String.join(", ", cities.keySet()));

        String city1Name = getValidStringInput("Введите название первого города: ", false);
        String city2Name = getValidStringInput("Введите название второго города: ", false);

        if (!cities.containsKey(city1Name) || !cities.containsKey(city2Name)) {
            System.out.println("Один из городов не найден!");
            return;
        }

        if (city1Name.equals(city2Name)) {
            System.out.println("Нельзя создать дорогу между одним и тем же городом!");
            return;
        }

        int cost = getValidIntInput(1, 1000, "Введите стоимость пути: ");

        City city1 = cities.get(city1Name);
        City city2 = cities.get(city2Name);

        city1.addRoad(city2, cost);
        System.out.println("Дорога между " + city1Name + " и " + city2Name + " добавлена со стоимостью " + cost);
    }

    private static void removeRoad() {
        if (cities.isEmpty()) {
            System.out.println("Нет созданных городов!");
            return;
        }

        System.out.println("\n--- Удаление дороги между городами ---");
        System.out.println("Доступные города: " + String.join(", ", cities.keySet()));

        String city1Name = getValidStringInput("Введите название первого города: ", false);
        String city2Name = getValidStringInput("Введите название второго города: ", false);

        if (!cities.containsKey(city1Name) || !cities.containsKey(city2Name)) {
            System.out.println("Один из городов не найден!");
            return;
        }

        City city1 = cities.get(city1Name);
        City city2 = cities.get(city2Name);

        if (!city1.getRoads().containsKey(city2)) {
            System.out.println("Дороги между этими городами не существует!");
            return;
        }

        city1.removeRoad(city2);
        System.out.println("Дорога между " + city1Name + " и " + city2Name + " удалена");
    }

    private static void showCities() {
        if (cities.isEmpty()) {
            System.out.println("Нет созданных городов!");
            return;
        }

        System.out.println("\n--- Список всех городов и их дорог ---");
        for (City city : cities.values()) {
            System.out.print("Город " + city.getName() + " соединен с: ");
            if (city.getRoads().isEmpty()) {
                System.out.println("нет дорог");
            } else {
                List<String> connections = new ArrayList<>();
                for (Map.Entry<City, Integer> road : city.getRoads().entrySet()) {
                    connections.add(road.getKey().getName() + " (стоимость: " + road.getValue() + ")");
                }
                System.out.println(String.join(", ", connections));
            }
        }
    }

    private static void findRoute() {
        if (cities.size() < 2) {
            System.out.println("Для поиска маршрута нужно как минимум 2 города!");
            return;
        }

        System.out.println("\n--- Поиск маршрута между городами ---");
        System.out.println("Доступные города: " + String.join(", ", cities.keySet()));

        String startName = getValidStringInput("Введите название начального города: ", false);
        String endName = getValidStringInput("Введите название конечного города: ", false);

        if (!cities.containsKey(startName) || !cities.containsKey(endName)) {
            System.out.println("Один из городов не найден!");
            return;
        }

        City start = cities.get(startName);
        City end = cities.get(endName);

        Route route = new Route(start, end);
        City[] path = route.getRoute();

        System.out.println("\nРезультат поиска маршрута:");
        System.out.println("Маршрут в строковом виде: " + route);

        if (path.length == 0) {
            System.out.println("Путь между городами не найден!");
        } else {
            System.out.println("Массив городов: " + Arrays.toString(path));
            System.out.println("Количество городов в пути: " + path.length);
        }
    }

    private static void interactiveCityManagement() {
        System.out.println("\n=== ИНТЕРАКТИВНОЕ УПРАВЛЕНИЕ ГОРОДАМИ ===");

        while (true) {
            System.out.println("\n--- Меню управления городами ---");
            System.out.println("1 - Создать город");
            System.out.println("2 - Добавить дорогу");
            System.out.println("3 - Удалить дорогу");
            System.out.println("4 - Показать все города");
            System.out.println("5 - Найти маршрут");
            System.out.println("6 - Очистить все города");
            System.out.println("7 - Вернуться в главное меню");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите число от 1 до 7.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    createCity();
                    break;
                case 2:
                    addRoad();
                    break;
                case 3:
                    removeRoad();
                    break;
                case 4:
                    showCities();
                    break;
                case 5:
                    findRoute();
                    break;
                case 6:
                    cities.clear();
                    System.out.println("Все города очищены!");
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Неверный выбор! Введите число от 1 до 7.");
            }

            waitForEnter();
        }
    }

    // ========== МЕТОДЫ ДЕМОНСТРАЦИИ ==========

    private static void demonstrateFraction() {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ ДРОБЕЙ ===");

        System.out.println("\n--- Создание дробей ---");
        int num1 = getAnyIntInput("Введите числитель первой дроби: ");
        int den1 = getValidIntInput(1, 100, "Введите знаменатель первой дроби (1-100): ");

        int num2 = getAnyIntInput("Введите числитель второй дроби: ");
        int den2 = getValidIntInput(1, 100, "Введите знаменатель второй дроби (1-100): ");

        try {
            Fraction f1 = new Fraction(num1, den1);
            Fraction f2 = new Fraction(num2, den2);

            System.out.println("\nСозданные дроби (автоматически упрощены):");
            System.out.println("f1 = " + f1);
            System.out.println("f2 = " + f2);

            System.out.println("\nАрифметические операции:");
            System.out.println(f1 + " + " + f2 + " = " + f1.add(f2));
            System.out.println(f1 + " - " + f2 + " = " + f1.subtract(f2));
            System.out.println(f1 + " * " + f2 + " = " + f1.multiply(f2));
            System.out.println(f1 + " / " + f2 + " = " + f1.divide(f2));

            System.out.println("\nПреобразование к разным типам (методы из Number):");
            System.out.println(f1 + " как int: " + f1.intValue());
            System.out.println(f1 + " как long: " + f1.longValue());
            System.out.println(f1 + " как float: " + f1.floatValue());
            System.out.println(f1 + " как double: " + f1.doubleValue());

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void demonstrateCitiesAndRoutes() {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ ГОРОДОВ И МАРШРУТОВ ===");

        // Сохраняем текущие города пользователя
        Map<String, City> userCities = new HashMap<>(cities);

        // Создаем фиксированную карту городов как в задании 1.3.3
        cities.clear();

        City A = new City("A");
        City B = new City("B");
        City C = new City("C");
        City D = new City("D");
        City E = new City("E");
        City F = new City("F");

        // Добавляем дороги
        A.addRoad(B, 5);
        A.addRoad(F, 1);
        A.addRoad(D, 6);
        B.addRoad(C, 3);
        C.addRoad(D, 4);
        D.addRoad(E, 2);
        E.addRoad(F, 2);
        F.addRoad(B, 1);

        cities.put("A", A);
        cities.put("B", B);
        cities.put("C", C);
        cities.put("D", D);
        cities.put("E", E);
        cities.put("F", F);

        System.out.println("Создана фиксированная сеть городов (из задачи 1.3.3):");
        showCities();

        // Демонстрация маршрута из F в D
        System.out.println("\n--- Маршрут из Города F в Город D (требование задачи) ---");
        Route route = new Route(F, D);
        System.out.println("Маршрут: " + route);
        System.out.println("Массив городов: " + Arrays.toString(route.getRoute()));

        // Восстанавливаем города пользователя
        cities.clear();
        cities.putAll(userCities);

        System.out.println("\nХотите перейти в интерактивный режим управления городами? (да/нет)");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("да") || response.equals("yes") || response.equals("y")) {
            interactiveCityManagement();
        }
    }

    private static void demonstrateSummation() {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ СЛОЖЕНИЯ РАЗНЫХ ТИПОВ ===");

        System.out.println("Примеры из задания:");

        // 2 + 3/5 + 2.3
        Fraction f1 = new Fraction(3, 5);
        double sum1 = MathUtils.sum(2, f1, 2.3);
        System.out.println("2 + 3/5 + 2.3 = " + sum1);

        // 3.6 + 49/12 + 3 + 3/2
        Fraction f2 = new Fraction(49, 12);
        Fraction f3 = new Fraction(3, 2);
        double sum2 = MathUtils.sum(3.6, f2, 3, f3);
        System.out.println("3.6 + 49/12 + 3 + 3/2 = " + sum2);

        // 1/3 + 1
        Fraction f4 = new Fraction(1, 3);
        double sum3 = MathUtils.sum(f4, 1);
        System.out.println("1/3 + 1 = " + sum3);
    }

    private static void demonstratePower(String[] args) {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ ВОЗВЕДЕНИЯ В СТЕПЕНЬ ===");

        // Использование аргументов командной строки
        if (args.length >= 2) {
            try {
                System.out.println("Использование аргументов командной строки:");
                System.out.println("args[0] = " + args[0] + ", args[1] = " + args[1]);
                double result = MathUtils.power(args[0], args[1]);
                System.out.println(args[0] + " в степени " + args[1] + " = " + result);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка в аргументах командной строки: " + e.getMessage());
            }
        } else {
            System.out.println("Аргументы командной строки не предоставлены.");
        }

        // Интерактивный ввод
        System.out.println("\n--- Интерактивный ввод ---");
        System.out.print("Введите основание X: ");
        String xStr = scanner.nextLine();

        System.out.print("Введите показатель степени Y: ");
        String yStr = scanner.nextLine();

        try {
            double result = MathUtils.power(xStr, yStr);
            System.out.println(xStr + " в степени " + yStr + " = " + result);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Введите корректные целые числа.");
        }
    }

    private static void demonstratePointCloning() {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ КЛОНИРОВАНИЯ ТОЧКИ ===");

        System.out.print("Введите координату X точки: ");
        double x = scanner.nextDouble();

        System.out.print("Введите координату Y точки: ");
        double y = scanner.nextDouble();
        scanner.nextLine();

        Point original = new Point(x, y);
        Point cloned = original.clone();

        System.out.println("Оригинальная точка: " + original);
        System.out.println("Клонированная точка: " + cloned);

        // Изменяем клон
        System.out.println("\nИзменяем координаты клона...");
        cloned.setX(100);
        cloned.setY(200);

        System.out.println("Оригинальная точка после изменения клона: " + original);
        System.out.println("Клонированная точка после изменения: " + cloned);
        System.out.println("Точки равны: " + original.equals(cloned));
    }

    private static void printMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== КОМПЛЕКСНАЯ ДЕМОНСТРАЦИЯ РЕШЕНИЯ ВСЕХ ЗАДАЧ ===");
        System.out.println("Анянов Кирилл ИТ-4");
        System.out.println("Пакетная структура: ru.anyanov.[fraction|city|geometry|math|main]");
        System.out.println("=".repeat(70));
        System.out.println("1 - Демонстрация дробей (задачи 4, 1, 2)");
        System.out.println("2 - Демонстрация городов и маршрутов (задачи 10, 5)");
        System.out.println("3 - Интерактивное управление городами");
        System.out.println("4 - Демонстрация сложения (задача 1)");
        System.out.println("5 - Демонстрация возведения в степень (задача 3)");
        System.out.println("6 - Демонстрация клонирования точки (задача 4)");
        System.out.println("7 - Показать все демонстрации последовательно");
        System.out.println("0 - Выход");
        System.out.println("=".repeat(70));
        System.out.print("Выберите вариант: ");
    }

    private static int getMenuChoice() {
        while (true) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 0 && choice <= 7) {
                    return choice;
                } else {
                    System.out.println("Ошибка! Введите число от 0 до 7.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите число.");
                scanner.nextLine();
            }
        }
    }

    // ========== ГЛАВНЫЙ МЕТОД ==========

    public static void main(String[] args) {
        System.out.println("=== КОМПЛЕКСНАЯ ДЕМОНСТРАЦИЯ РЕШЕНИЯ ВСЕХ ЗАДАЧ ===");
        System.out.println("Анянов Кирилл ИТ-4");
        System.out.println("Пакетная структура: ru.anyanov.[fraction|city|geometry|math|main]");

        boolean running = true;

        while (running) {
            int choice = getMenuChoice();

            switch (choice) {
                case 0:
                    System.out.println("Выход из программы.");
                    running = false;
                    break;
                case 1:
                    demonstrateFraction();
                    waitForEnter();
                    break;
                case 2:
                    demonstrateCitiesAndRoutes();
                    waitForEnter();
                    break;
                case 3:
                    interactiveCityManagement();
                    break;
                case 4:
                    demonstrateSummation();
                    waitForEnter();
                    break;
                case 5:
                    demonstratePower(args);
                    waitForEnter();
                    break;
                case 6:
                    demonstratePointCloning();
                    waitForEnter();
                    break;
                case 7:
                    demonstrateFraction();
                    waitForEnter();
                    demonstrateCitiesAndRoutes();
                    waitForEnter();
                    demonstrateSummation();
                    waitForEnter();
                    demonstratePower(args);
                    waitForEnter();
                    demonstratePointCloning();
                    waitForEnter();
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        }

        scanner.close();
    }
}