package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealsDao {
    Meal add(Meal meal);

    Collection<Meal> getAll();

    Meal update(Meal meal);

    void delete(long id);

    Meal getById(long id);
}
