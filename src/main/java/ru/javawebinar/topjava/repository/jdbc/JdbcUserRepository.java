package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    private final static ResultSetExtractor<List<User>> LIST_RESULT_SET_EXTRACTOR = rs -> {
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
            String role = rs.getString("roles");
            if (role != null) roles.addAll(Arrays.stream(role.split(",")).map(Role::valueOf).toList());
            user.setRoles(roles);
            users.add(user);
        }
        return users;
    };
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;
    private final BeanPropertyRowMapper<User> userBeanPropertyRowMapper = BeanPropertyRowMapper.newInstance(User.class);

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
            this.insertBatchData(user, userId);
            user.setId(userId);
        } else {
            boolean isUpdatedUser = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0;
            List<Role> rolesData = this.jdbcTemplate.queryForList("SELECT role FROM user_role WHERE user_id=?", Role.class, user.getId());
            Set<Role> roles = !rolesData.isEmpty() ? EnumSet.copyOf(rolesData) : EnumSet.noneOf(Role.class);
            boolean isUpdatedRole = !roles.equals(EnumSet.copyOf(user.getRoles()));
            if (isUpdatedRole) {
                jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.id());
                this.insertBatchData(user, user.id());
            }
            if (!isUpdatedUser && !isUpdatedRole)
                return null;
        }
        return user;
    }

    private void insertBatchData(User user, int user1) {
        jdbcTemplate.batchUpdate("INSERT INTO user_role (role, user_id) VALUES (?, ?)", user.getRoles(),
                200,
                (ps, argument) -> {
                    ps.setString(1, argument.name());
                    ps.setInt(2, user1);
                });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("""
                        SELECT users.id,
                               users.name,
                               users.email,
                               users.password,
                               users.registered,
                               users.enabled,
                               users.calories_per_day,
                               STRING_AGG(user_role.role, ',') AS roles
                        FROM users
                                 LEFT JOIN user_role ON users.id = user_role.user_id
                        WHERE users.id=?
                        GROUP BY users.id, users.name, users.email, users.password, users.registered, users.enabled, users.calories_per_day
                        """,
                this.userBeanPropertyRowMapper, id));
    }

    @Override
    public User getByEmail(String email) {
        return DataAccessUtils.singleResult(this.jdbcTemplate.query("""
                        SELECT users.id,
                               users.name,
                               users.email,
                               users.password,
                               users.registered,
                               users.enabled,
                               users.calories_per_day,
                               STRING_AGG(user_role.role, ',') AS roles
                        FROM users
                                 LEFT JOIN user_role ON users.id = user_role.user_id
                        WHERE users.email=?
                        GROUP BY users.id, users.name, users.email, users.password, users.registered, users.enabled, users.calories_per_day
                        """,
                this.userBeanPropertyRowMapper, email));
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
                       STRING_AGG(user_role.role, ',') AS roles
                FROM users
                         LEFT JOIN user_role ON users.id = user_role.user_id
                GROUP BY users.id, users.name, users.email, users.password, users.registered, users.enabled, users.calories_per_day
                ORDER BY users.name, users.email""", this.userBeanPropertyRowMapper);
    }
}
