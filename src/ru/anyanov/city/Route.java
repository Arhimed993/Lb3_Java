package ru.anyanov.city;

import java.util.*;

public class Route {
    private City start;
    private City end;

    public Route(City start, City end) {
        setStart(start);
        setEnd(end);
    }

    public void setStart(City start) {
        if (start == null) {
            throw new IllegalArgumentException("Город начала не может быть null");
        }
        this.start = start;
    }

    public void setEnd(City end) {
        if (end == null) {
            throw new IllegalArgumentException("Город конца не может быть null");
        }
        this.end = end;
    }

    public City getStart() { return start; }
    public City getEnd() { return end; }

    /**
     * Возвращает массив городов, представляющий маршрут из начала в конец.
     * Используется алгоритм BFS (поиск в ширину) для нахождения пути
     * через наименьшее число городов.
     * Сложность: O(V + E), где V - количество вершин, E - количество рёбер
     */
    public City[] getRoute() {
        // Если начало и конец совпадают
        if (start.equals(end)) {
            return new City[]{start};
        }

        // Алгоритм BFS для поиска кратчайшего пути по количеству городов
        Map<City, City> previous = new HashMap<>(); // Для восстановления пути
        Queue<City> queue = new LinkedList<>();
        Set<City> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        previous.put(start, null);

        while (!queue.isEmpty()) {
            City current = queue.poll();

            // Проверяем всех соседей
            for (City neighbor : current.getRoads().keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    previous.put(neighbor, current);
                    queue.add(neighbor);

                    // Если нашли конечный город
                    if (neighbor.equals(end)) {
                        return buildPath(previous);
                    }
                }
            }
        }

        return new City[0]; // Путь не найден
    }

    private City[] buildPath(Map<City, City> previous) {
        List<City> path = new ArrayList<>();
        City current = end;

        // Восстанавливаем путь от конца к началу
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        // Разворачиваем путь, чтобы получить от начала к концу
        Collections.reverse(path);
        return path.toArray(new City[0]);
    }

    @Override
    public String toString() {
        City[] route = getRoute();
        if (route.length == 0) {
            return "Маршрут не найден";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < route.length; i++) {
            sb.append(route[i].getName());
            if (i < route.length - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}