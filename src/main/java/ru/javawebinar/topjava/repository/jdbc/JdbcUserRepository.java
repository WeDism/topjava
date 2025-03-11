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
import ru.javawebinar.topjava.repository.ValidatorUtil;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;
    private final ResultSetExtractor<User> userResultSetExtractor = rs -> {
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setName(rs.getString(2));
            user.setEmail(rs.getString(3));
            user.setPassword(rs.getString(4));
            user.setRegistered(rs.getDate(5));
            user.setEnabled(rs.getBoolean(6));
            user.setCaloriesPerDay(rs.getInt(7));
            HashSet<Role> roles = new HashSet<>();
            roles.add(Role.valueOf(rs.getString(9)));
            while (rs.next()) {
                roles.add(Role.valueOf(rs.getString(9)));
            }
            user.setRoles(roles);
            return user;
        } else return null;
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
        ValidatorUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            jdbcTemplate.batchUpdate("INSERT INTO user_role (role, user_id) VALUES (?, ?)", user.getRoles(),
                    200,
                    (ps, argument) -> {
                        ps.setString(1, argument.name());
                        ps.setInt(2, newKey.intValue());
                    });
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0
                    & jdbcTemplate.batchUpdate("UPDATE user_role SET role=? WHERE user_id=?", user.getRoles(),
                    200,
                    (ps, argument) -> {
                        ps.setString(1, argument.name());
                        ps.setInt(2, user.id());
                    }).length == 0) {
                return null;
            }
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
        return jdbcTemplate.query("SELECT users.*, user_role.* FROM users join user_role on users.id = user_role.user_id WHERE id=?", this.userResultSetExtractor, id);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return jdbcTemplate.query("SELECT * FROM users join user_role on users.id = user_role.user_id WHERE email=?", this.userResultSetExtractor, email);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT users.*, user_role.* FROM users LEFT JOIN user_role ON users.id = user_role.user_id", rs -> {
            Map<Integer, User> userHashMap = new HashMap<>();
            while (rs.next()) {
                User userOld = userHashMap.get(rs.getInt(1));
                String roleAsString = rs.getString(9);
                Role role = roleAsString != null && !roleAsString.isEmpty() ? Role.valueOf(roleAsString) : null;
                if (userOld != null && role != null) userOld.getRoles().add(role);
                else {
                    User user = new User();
                    user.setId(rs.getInt(1));
                    user.setName(rs.getString(2));
                    user.setEmail(rs.getString(3));
                    user.setPassword(rs.getString(4));
                    user.setRegistered(rs.getDate(5));
                    user.setEnabled(rs.getBoolean(6));
                    user.setCaloriesPerDay(rs.getInt(7));
                    HashSet<Role> roles = new HashSet<>();
                    if (role != null) roles.add(role);
                    user.setRoles(roles);
                    userHashMap.put(user.getId(), user);
                }
            }
            return userHashMap.values().stream().sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail)).toList();
        });
    }
}
