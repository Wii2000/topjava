package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_MEAL_ID = START_SEQ + 2;

    public static final Meal USER_MEAL_1 = new Meal(USER_MEAL_ID, LocalDateTime.of(2021, 06, 14, 10, 00), "Пользователь завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2021, 06, 14, 13, 00), "Пользователь обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2021, 06, 14, 20, 00), "Пользователь ужин", 500);
    public static final Meal USER_MEAL_4 = new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2021, 06, 15, 00, 00), "Пользователь еда на граничном значении", 100);
    public static final Meal USER_MEAL_5 = new Meal(USER_MEAL_ID + 4, LocalDateTime.of(2021, 06, 15, 10, 00), "Пользователь завтрак", 1000);
    public static final Meal USER_MEAL_6 = new Meal(USER_MEAL_ID + 5, LocalDateTime.of(2021, 06, 15, 13, 00), "Пользователь обед", 500);
    public static final Meal USER_MEAL_7 = new Meal(USER_MEAL_ID + 6, LocalDateTime.of(2021, 06, 15, 20, 00), "Пользователь ужин", 410);
    public static final Meal ADMIN_MEAL_1 = new Meal(USER_MEAL_ID + 7, LocalDateTime.of(2021, 06, 14, 14, 00), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL_2 = new Meal(USER_MEAL_ID + 8, LocalDateTime.of(2021, 06, 14, 21, 00), "Админ ужин", 1500);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2021, 06, 20, 10, 00), "New meal", 1500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDateTime(LocalDateTime.of(2021, 06, 14, 15, 30));
        updated.setDescription("Updated meal");
        updated.setCalories(100);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
