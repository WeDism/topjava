package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);
    private final MealRestController mealController;

    @Autowired
    public JspMealController(MealRestController mealController) {
        this.mealController = mealController;
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.debug("post create {}", meal);
        mealController.create(meal);
        return "redirect:meals";
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request) throws IOException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int id = this.getId(request);
        mealController.update(meal, id);
        log.debug("post update {} id {}", meal, id);
        return "redirect:meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) throws ServletException, IOException {
        int id = this.getId(request);
        mealController.delete(id);
        log.debug("get delete id {}", id);
        return "redirect:meals";
    }

    @GetMapping("/add")
    public String getCreate(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        log.debug("get add meal {} action create", meal);
        return "mealForm";
    }

    @GetMapping("/update")
    public String getUpdate(HttpServletRequest request) {
        Meal meal = mealController.get(this.getId(request));
        request.setAttribute("meal", meal);
        log.debug("get update {}", meal);
        return "mealForm";
    }

    @GetMapping("/filter")
    public String getFiltered(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> between = mealController.getBetween(startDate, startTime, endDate, endTime);
        request.setAttribute("meals", between);
        log.debug("get filter meals {} startDate {} endDate {} startTime {} endTime {}", between, startDate, endDate, startTime, endTime);
        return "meals";
    }

    @GetMapping("/meals")
    public String getAll(HttpServletRequest request) throws ServletException, IOException {
        List<MealTo> all = mealController.getAll();
        request.setAttribute("meals", all);
        log.debug("get meals {} ", all);
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
