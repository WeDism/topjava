package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.data.DataRepository;
import ru.javawebinar.topjava.data.DataRepositoryImpl;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class EditMealServlet extends HttpServlet {
    DataRepository dataRepository = DataRepositoryImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(String.valueOf(request.getParameter("id")));
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("mealTo", dataRepository.getMeals().stream().filter(meal -> meal.getId() == id).findFirst().orElse(null));
        request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(String.valueOf(request.getParameter("id")));
        LocalDateTime datetime = LocalDateTime.parse(String.valueOf(request.getParameter("datetime")));
        String description = String.valueOf(request.getParameter("description"));
        int calories = Integer.parseInt(String.valueOf(request.getParameter("calories")));
        Optional<Meal> optionalMealTo = this.dataRepository.getMeals().stream().filter(meal -> meal.getId() == id).findFirst();
        if (optionalMealTo.isPresent()) {
            this.dataRepository.getMeals().remove(optionalMealTo.get());
            this.dataRepository.getMeals().add(new Meal(id, datetime, description, calories));
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
