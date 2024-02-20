package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.data.InMemoryMealRepository;
import ru.javawebinar.topjava.data.MealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
    private MealRepository mealRepository;

    @Override
    public void init() {
        this.mealRepository = InMemoryMealRepository.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String actionText = request.getParameter("action");
        if (actionText != null && !actionText.isEmpty()) {
            Action action = Action.valueOf(actionText.toUpperCase());
            if (action.isUpdate()) {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("meal", this.mealRepository.getById(id));
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
                return;
            } else if (action.isDelete()) {
                int id = Integer.parseInt(request.getParameter("id"));
                this.mealRepository.delete(id);
                response.sendRedirect(request.getContextPath() + "/meals");
                return;
            } else if (action.isCreate()) {
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
                return;
            }
        }
        request.setAttribute("meals", MealsUtil.filteredByStreams(this.mealRepository.getAll(), MealsUtil.CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String idText = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (idText == null || idText.isEmpty()) {
            this.mealRepository.create(new Meal(dateTime, description, calories));
        } else {
            int id = Integer.parseInt(idText);
            this.mealRepository.update(new Meal(id, dateTime, description, calories));
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
