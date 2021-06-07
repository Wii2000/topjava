package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryDbMealsDao;
import ru.javawebinar.topjava.dao.MealsDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;
    private MealsDao mealsDao;

    @Override
    public void init() throws ServletException {
        mealsDao = new InMemoryDbMealsDao();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        log.debug("Start processing get request");
        String action = request.getParameter("action");

        if (action == null) {
            action = "unknown";
        }

        switch (action) {
            case "edit":
                log.debug("Forward to meals edit form");
                request.setAttribute("meal",
                        mealsDao.getById(Long.parseLong(request.getParameter("id"))));
                request.getRequestDispatcher("mealsEdit.jsp").forward(request, response);
                break;
            case "add":
                log.debug("Forward to meals create form");
                request.getRequestDispatcher("mealsEdit.jsp").forward(request, response);
                break;
            case "delete":
                log.debug("Deleting meal and redirect to meals list");
                mealsDao.delete(Long.parseLong(request.getParameter("id")));
                response.sendRedirect("meals");
                break;
            default:
                log.debug("Forward to meals list");
                request.setAttribute("meals", MealsUtil.filteredByStreams(
                        mealsDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT));
                request.getRequestDispatcher("mealsList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        log.debug("Start processing post request");
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime"),
                        DateTimeFormatter.ISO_DATE_TIME),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            log.debug("Adding new meal to DB");
            mealsDao.add(meal);
        } else {
            log.debug("Updating meal in DB");
            meal.setId(Long.parseLong(id));
            mealsDao.update(meal);
        }

        log.debug("Redirect to meals list");
        response.sendRedirect("meals");
    }
}
