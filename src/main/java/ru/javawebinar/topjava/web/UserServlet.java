package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int user = Integer.parseInt(request.getParameter("user"));
        log.debug("SecurityUtil userId: {}", user);
        if (user == 1)
            SecurityUtil.setUser1();
        else if (user == 2)
            SecurityUtil.setUser2();
        else log.error("такого пользователя нет!");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
