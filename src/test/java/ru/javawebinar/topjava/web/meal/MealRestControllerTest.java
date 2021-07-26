package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';
    public static final MatcherFactory<MealTo> MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(MealTo.class);

    @Autowired
    private MealRestController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void getBetween() throws Exception {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", "2020-12-31");
        params.add("endDate", "2020-12-31");
        params.add("startTime", "10:00");
        params.add("endTime", "20:00");
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(controller.getBetween(
                        parseLocalDate(params.getFirst("startDate")),
                        parseLocalTime(params.getFirst("startTime")),
                        parseLocalDate(params.getFirst("endDate")),
                        parseLocalTime(params.getFirst("endTime"))
                )));
    }

    @Test
    void getBetweenWithNull() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(controller.getBetween(null, null, null, null)));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(MEAL1_ID));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MealTestData.MATCHER.assertMatch(controller.get(MEAL1_ID), updated);
    }

    @Test
    void createMeal() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MealTestData.MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MealTestData.MATCHER.assertMatch(created, newMeal);
        MealTestData.MATCHER.assertMatch(controller.get(newId), newMeal);
    }
}