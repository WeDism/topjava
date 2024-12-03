package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id and m.user.id=:user_id"),
        @NamedQuery(name = Meal.BY_USER, query = "SELECT m FROM Meal m WHERE m.id=?1 AND m.user.id=?2"),
        @NamedQuery(name = Meal.BY_BETWEEN_HALF_OPEN,
                query = "SELECT m FROM Meal m WHERE m.user.id=?1 and m.dateTime>=?2 and m.dateTime<?3 ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.BY_ALL_SORTED,
                query = "SELECT m FROM Meal m WHERE m.user.id=?1 ORDER BY m.dateTime DESC")
})
@Entity
@Table(name = "meal", indexes =
        {@Index(name = "meal_unique_user_datetime_idx", columnList = "user_id, date_time", unique = true)})
public class Meal extends AbstractBaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String BY_BETWEEN_HALF_OPEN = "Meal.getByBetweenHalfOpen";
    public static final String BY_USER = "Meal.getByUser";
    public static final String BY_ALL_SORTED = "Meal.getAllSorted";
    @NotNull
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
    @NotBlank
    @Size(min = 2, max = 120)
    @Column(name = "description", nullable = false)
    private String description;
    @Range(min = 10, max = 5000)
    @Column(name = "calories", nullable = false)
    private int calories;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
