package ru.javawebinar.topjava.repository.jdbc.hsql;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.jdbc.common.JdbcMealRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
@Profile(Profiles.HSQL_DB)
public class HsqlJdbcMealRepository extends JdbcMealRepository {
    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Object getLocalDateTimeByDBMS(LocalDateTime localDateTime) {
        return Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
