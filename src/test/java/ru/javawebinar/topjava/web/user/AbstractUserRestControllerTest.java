package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;

public abstract class AbstractUserRestControllerTest extends AbstractControllerTest {

    @Autowired
    private Environment env;

    protected boolean isDataJpaBased() {
        return env.acceptsProfiles(org.springframework.core.env.Profiles.of(Profiles.DATAJPA));
    }

    protected void getWithMeals(String url) throws Exception {
        if (isDataJpaBased()) {
            User expectedUser = new User(user);

            ResultActions action = perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

            User user = MATCHER.readFromJson(action);
            MATCHER.assertMatch(user, expectedUser);
            MealTestData.MATCHER.assertMatch(user.getMeals(), MealTestData.meals);
        } else {
            assertThrows(UnsupportedOperationException.class, () -> {
                try {
                    perform(MockMvcRequestBuilders.get(url));
                } catch (Exception e) {
                    throw getRootCause(e);
                }
            });
        }
    }
}
