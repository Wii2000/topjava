package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDbMealsDao implements MealsDao {

    private static final AtomicLong id = new AtomicLong();

    private Map<Long, Meal> meals;

    public InMemoryDbMealsDao() {
        meals = new ConcurrentHashMap<>();
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(id.get(), new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(id.getAndIncrement());
        meals.put(meal.getId(), meal);
    }

    @Override
    public Collection<Meal> getAll() {
        return meals.values();
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
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
