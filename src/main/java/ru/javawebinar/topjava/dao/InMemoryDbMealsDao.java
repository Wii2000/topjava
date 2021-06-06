package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDbMealsDao implements MealsDao {
  private static final int CALORIES_LIMIT = 2000;

  private static final AtomicLong id = new AtomicLong();

  private static List<Meal> meals = new CopyOnWriteArrayList<>(Arrays.asList(
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
          new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
  ));

  @Override
  public void add(Meal meal) {
    meal.setId(id.getAndIncrement());
    meals.add(meal);
  }

  @Override
  public List<MealTo> getAll() {
    return MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);
  }

  @Override
  public void update(Meal meal) {
    delete(meal.getId());
    meals.add(meal);
  }

  @Override
  public void delete(long id) {
    for(int i = 0; i < meals.size(); i++) {
      Meal meal = meals.get(i);
      if (meal.getId() == id) {
        meals.remove(i);
        break;
      }
    }
  }

  @Override
  public Meal getById(long id) {
    for (Meal meal : meals) {
      if (meal.getId() == id) {
        return meal;
      }
    }
    return null;
  }
}
