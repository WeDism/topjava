package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;
    private final ResultSetExtractor<List<User>> userResultSetExtractor = rs -> {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRegistered(rs.getDate("registered"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            EnumSet<Role> roles = EnumSet.noneOf(Role.class);
            String role = rs.getString("role");
            if (role != null && !role.isEmpty()) roles.addAll(Arrays.stream(role.split(",")).map(Role::valueOf).toList());
            user.setRoles(roles);
            users.add(user);
        }
        return users;
    };

    private final ResultSetExtractor<List<Role>> roleResultSetExtractor = rs -> {
        List<Role> roles = new ArrayList<>();
        while (rs.next())
            roles.add(Role.valueOf(rs.getString("role")));
        return roles;
    };

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            int userId = newKey.intValue();
            jdbcTemplate.batchUpdate("INSERT INTO user_role (role, user_id) VALUES (?, ?)", user.getRoles(),
                    200,
                    (ps, argument) -> {
                        ps.setString(1, argument.name());
                        ps.setInt(2, userId);
                    });
            user.setId(userId);
        } else {
            boolean isUpdatedUser = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0;
            List<Role> rolesData = this.jdbcTemplate.query("select * from user_role where user_id=?", this.roleResultSetExtractor, user.getId());
            Set<Role> roles = rolesData != null && !rolesData.isEmpty() ? EnumSet.copyOf(rolesData) : EnumSet.noneOf(Role.class);
            boolean isUpdatedRole = !roles.equals(EnumSet.copyOf(user.getRoles()));
            if (isUpdatedRole) {
                jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.id());
                jdbcTemplate.batchUpdate("insert into user_role (role,user_id) values (?,?)", user.getRoles(),
                        200,
                        (ps, argument) -> {
                            ps.setString(1, argument.name());
                            ps.setInt(2, user.id());
                        });
            }
            if (!isUpdatedUser && !isUpdatedRole)
                return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return Objects.requireNonNull(jdbcTemplate.query("""
                        SELECT users.id,
                               users.name,
                               users.email,
                               users.password,
                               users.registered,
                               users.enabled,
                               users.calories_per_day,
                               STRING_AGG(user_role.role, ',') as role
                        FROM users
                                 LEFT JOIN user_role ON users.id = user_role.user_id
                        where users.id=?
                        group by users.id, users.name, users.email, users.password, users.registered, users.enabled, users.calories_per_day
                        """,
                this.userResultSetExtractor, id)).stream().findFirst().orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return Objects.requireNonNull(this.jdbcTemplate.query("""
                        SELECT users.id,
                               users.name,
                               users.email,
                               users.password,
                               users.registered,
                               users.enabled,
                               users.calories_per_day,
                               STRING_AGG(user_role.role, ',') as role
                        FROM users
                                 LEFT JOIN user_role ON users.id = user_role.user_id
                        where users.email=?
                        group by users.id, users.name, users.email, users.password, users.registered, users.enabled, users.calories_per_day
                        """,
                this.userResultSetExtractor, email)).stream().findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query("""
                SELECT users.id,
                       users.name,
                       users.email,
                       users.password,
                       users.registered,
                       users.enabled,
                       users.calories_per_day,
                       STRING_AGG(user_role.role, ',') as role
                FROM users
                         LEFT JOIN user_role ON users.id = user_role.user_id
                group by users.id, users.name, users.email, users.password, users.registered, users.enabled, users.calories_per_day
                order by users.name""", this.userResultSetExtractor);
    }
}
