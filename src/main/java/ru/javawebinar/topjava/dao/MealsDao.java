package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealsDao {
  void add(Meal meal);
  List<MealTo> getAll();
  void update(Meal meal);
  void delete(long id);
  Meal getById(long id);
}
