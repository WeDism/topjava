package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.data.DataRepository;
import ru.javawebinar.topjava.data.DataRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MealServlet extends HttpServlet {
    DataRepository dataRepository = DataRepositoryImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("meals", MealsUtil.filteredByStreamsWithTestData(dataRepository.getMeals()));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
