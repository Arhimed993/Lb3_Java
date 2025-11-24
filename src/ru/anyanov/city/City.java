package ru.anyanov.city;

import java.util.*;

public final class City {
    private final String name;
    private final Map<City, Integer> roads;

    public City(String name) {
        this.name = name;
        this.roads = new HashMap<>();
    }

    public City(String name, Map<City, Integer> initialRoads) {
        this.name = name;
        this.roads = new HashMap<>();
        if (initialRoads != null) {
            for (Map.Entry<City, Integer> entry : initialRoads.entrySet()) {
                addRoad(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addRoad(City city, int cost) {
        // Гарантируем, что между двумя городами только одна дорога
        if (city != this && !roads.containsKey(city)) {
            roads.put(city, cost);
            city.roads.put(this, cost); // Дорога двусторонняя
        }
    }

    public void removeRoad(City city) {
        if (roads.containsKey(city)) {
            roads.remove(city);
            city.roads.remove(this);
        }
    }

    public String getName() {
        return name;
    }

    public Map<City, Integer> getRoads() {
        return new HashMap<>(roads);
    }

    // Метод для получения имен соседних городов
    public Set<String> getNeighborNames() {
        Set<String> neighborNames = new HashSet<>();
        for (City neighbor : roads.keySet()) {
            neighborNames.add(neighbor.name);
        }
        return neighborNames;
    }

    // Метод для красивого вывода информации о городе
    public String getRoadsInfo() {
        if (roads.isEmpty()) {
            return "нет дорог";
        }

        List<String> roadInfo = new ArrayList<>();
        for (Map.Entry<City, Integer> road : roads.entrySet()) {
            roadInfo.add(road.getKey().name + " (стоимость: " + road.getValue() + ")");
        }
        return String.join(", ", roadInfo);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City other = (City) obj;

        // Сравниваем по именам и наборам соседей (без рекурсии)
        return Objects.equals(name, other.name) &&
                Objects.equals(this.getNeighborNames(), other.getNeighborNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getNeighborNames());
    }
}