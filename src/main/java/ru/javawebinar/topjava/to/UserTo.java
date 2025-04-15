package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.model.Role;

import java.util.*;

public class UserTo {
    private final Integer id;
    private final String name;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Date registered;
    private final Set<Role> roles;
    private final int caloriesPerDay;
    private final List<MealTo> meals;

    public UserTo(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Date registered, Collection<Role> roles, List<MealTo> mealTos) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        this.registered = registered;
        this.meals = new ArrayList<>(mealTos);
        this.roles = new HashSet<>(roles);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public Date getRegistered() {
        return registered;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public List<MealTo> getMeals() {
        return meals;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", registered=" + registered +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                ", meals=" + meals +
                '}';
    }
}