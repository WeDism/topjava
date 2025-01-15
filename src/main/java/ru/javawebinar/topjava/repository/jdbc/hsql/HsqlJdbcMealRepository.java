package ru.javawebinar.topjava.repository.jdbc.hsql;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.jdbc.common.JdbcMealRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
@Profile(Profiles.HSQL_DB)
public class HsqlJdbcMealRepository extends JdbcMealRepository {
    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Timestamp startDate = Timestamp.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp endDate = Timestamp.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return super.jdbcTemplate.query(
                "SELECT * FROM meal WHERE user_id=?  AND date_time >=  ? AND date_time < ? ORDER BY date_time DESC",
                JdbcMealRepository.ROW_MAPPER, userId, startDate, endDate);
    }

}
