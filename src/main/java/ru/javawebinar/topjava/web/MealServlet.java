package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.InMemoryDbMealsDao;
import ru.javawebinar.topjava.dao.MealsDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
  private MealsDao mealsDao;

  @Override
  public void init() throws ServletException {
    super.init();
    mealsDao = new InMemoryDbMealsDao();
  }

  @Override
  protected void doGet(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws ServletException, IOException {
    String action = request.getParameter("action");
    if (action != null && action.equals("delete")) {
      mealsDao.delete(Long.parseLong(request.getParameter("id")));
      response.sendRedirect("meals");
    } else if (action != null && action.equals("edit")) {
      request.setAttribute("meal", mealsDao.getById(Long.parseLong(request.getParameter("id"))));
      request.getRequestDispatcher("mealsEdit.jsp").forward(request, response);
    } else if (action != null && action.equals("add")) {
      request.getRequestDispatcher("mealsEdit.jsp").forward(request, response);
    } else {
      request.setAttribute("meals", mealsDao.getAll());
      request.getRequestDispatcher("mealsList.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    Meal meal = new Meal(
            LocalDateTime.parse(request.getParameter("dateTime"), TimeUtil.getFormatter()),
            request.getParameter("description"),
            Integer.parseInt(request.getParameter("calories"))
    );

    String id = request.getParameter("id");
    if (id == null || id.isEmpty()) {
      mealsDao.add(meal);
    } else {
      meal.setId(Long.parseLong(id));
      mealsDao.update(meal);
    }

    response.sendRedirect("meals");
  }
}
