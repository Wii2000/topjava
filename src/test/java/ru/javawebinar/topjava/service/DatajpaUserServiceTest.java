package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles("datajpa")
public class DatajpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeal() {
        User user = service.get(USER_ID);
        MATCHER.assertMatch(user, UserTestData.user);
    }
}
