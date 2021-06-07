package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealsDao {
    void add(Meal meal);

    Collection<Meal> getAll();

    void update(Meal meal);

    void delete(long id);

    Meal getById(long id);
}
