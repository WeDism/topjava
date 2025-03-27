package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
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

@Controller
@RequestMapping("meals")
public class JspMealController extends MealController {

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping("create")
    public String create(HttpServletRequest request) throws IOException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        super.log.info("post create {}", meal);
        super.create(meal);
        return "redirect:/meals";
    }

    @PostMapping("update")
    public String update(HttpServletRequest request) throws IOException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int id = this.getId(request);
        super.update(meal, id);
        super.log.info("post update {} id {}", meal, id);
        return "redirect:/meals";
    }

    @GetMapping("delete")
    public String delete(HttpServletRequest request) throws ServletException, IOException {
        int id = this.getId(request);
        super.delete(id);
        super.log.info("get delete id {}", id);
        return "redirect:/meals";
    }

    @GetMapping("add")
    public String getCreate(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        super.log.info("get add meal {} action create", meal);
        return "mealForm";
    }

    @GetMapping("update")
    public String getUpdate(HttpServletRequest request) {
        Meal meal = super.get(this.getId(request));
        request.setAttribute("meal", meal);
        super.log.info("get update {}", meal);
        return "mealForm";
    }

    @GetMapping("filter")
    public String getFiltered(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> between = super.getBetween(startDate, startTime, endDate, endTime);
        request.setAttribute("meals", between);
        super.log.trace("get filter meals {} startDate {} endDate {} startTime {} endTime {}", between, startDate, endDate, startTime, endTime);
        return "meals";
    }

    @GetMapping
    public String getAll(HttpServletRequest request) throws ServletException, IOException {
        List<MealTo> all = super.getAll();
        request.setAttribute("meals", all);
        super.log.trace("get meals {} ", all);
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
