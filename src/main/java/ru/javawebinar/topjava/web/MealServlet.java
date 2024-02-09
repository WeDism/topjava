package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.data.DataMealInMemoryRepository;
import ru.javawebinar.topjava.data.DataMealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
    private final DataMealRepository dataMealInMemoryRepository = DataMealInMemoryRepository.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String actionText = request.getParameter("action");
        if (actionText != null && !actionText.isEmpty()) {
            Action action = Action.valueOf(actionText.toUpperCase());
            if (action.isUpdate()) {
                int id = Integer.parseInt(String.valueOf(request.getParameter("id")));
                request.setAttribute("meal", this.dataMealInMemoryRepository.get(id));
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
            } else if (action.isDelete()) {
                int id = Integer.parseInt(String.valueOf(request.getParameter("id")));
                this.dataMealInMemoryRepository.delete(id);
                request.setAttribute("meals", MealsUtil.filteredByStreams(this.dataMealInMemoryRepository.get().values()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            } else if (action.isCreate()) {
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("meals", MealsUtil.filteredByStreams(this.dataMealInMemoryRepository.get().values()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String idText = String.valueOf(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(String.valueOf(request.getParameter("dateTime")));
        String description = String.valueOf(request.getParameter("description"));
        int calories = Integer.parseInt(String.valueOf(request.getParameter("calories")));
        if (idText == null || idText.isEmpty()) {
            this.dataMealInMemoryRepository.create(new Meal(dateTime, description, calories));
        } else {
            int id = Integer.parseInt(idText);
            this.dataMealInMemoryRepository.update(new Meal(id, dateTime, description, calories));
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
