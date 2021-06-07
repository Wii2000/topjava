package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDbMealsDao implements MealsDao {

    private final AtomicLong id = new AtomicLong();

    private Map<Long, Meal> meals;

    public InMemoryDbMealsDao() {
        meals = new ConcurrentHashMap<>();
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(id.getAndIncrement());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Collection<Meal> getAll() {
        return meals.values();
    }

    @Override
    public Meal update(Meal meal) {
        if (meals.containsKey(meal.getId())) {
            meals.put(meal.getId(), meal);
            return meal;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        meals.remove(id);
    }

    @Override
    public Meal getById(long id) {
        return meals.get(id);
    }
}
