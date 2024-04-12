package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        this.appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        this.mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                null);
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew())
            this.mealRestController.create(meal);
        else this.mealRestController.update(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterDate = request.getParameter("filterDate");
        if (filterDate != null && !filterDate.isEmpty()) {
            String startDateText = request.getParameter("startDate");
            String startTimeText = request.getParameter("startTime");
            String endDateText = request.getParameter("endDate");
            String endTimeText = request.getParameter("endTime");
            LocalDate startDate = Objects.isNull(startDateText) || startDateText.isEmpty()
                    ? LocalDate.MIN
                    : LocalDate.parse(startDateText);
            LocalDate endDate = Objects.isNull(endDateText) || endDateText.isEmpty()
                    ? LocalDate.MAX
                    : LocalDate.parse(endDateText);
            LocalTime startTime = Objects.isNull(startTimeText) || startTimeText.isEmpty()
                    ? LocalTime.MIN
                    : LocalTime.parse(startTimeText);
            LocalTime endTime = Objects.isNull(endTimeText) || endTimeText.isEmpty()
                    ? LocalTime.MAX
                    : LocalTime.parse(endTimeText);
            request.setAttribute("meals", this.mealRestController.getAllByUserWithDateTimeFiltered(startTime, endTime, startDate, endDate));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String action = request.getParameter("action");
            int userId = SecurityUtil.authUserId();
            switch (action == null ? "all" : action) {
                case "delete":
                    int id = this.getId(request);
                    log.info("Delete id={}", id);
                    this.mealRestController.delete(id);
                    response.sendRedirect("meals");
                    break;
                case "create":
                case "update":
                    final Meal meal = "create".equals(action) ?
                            new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, userId) :
                            this.mealRestController.get(this.getId(request));
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                    break;
                case "all":
                default:
                    log.info("getAll");
                    request.setAttribute("meals", this.mealRestController.getAllByUser());
                    request.getRequestDispatcher("/meals.jsp").forward(request, response);
                    break;
            }
        }
    }

    @Override
    public void destroy() {
        this.appCtx.close();
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
