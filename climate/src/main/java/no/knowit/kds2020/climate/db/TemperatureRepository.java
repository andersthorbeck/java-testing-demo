package no.knowit.kds2020.climate.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@SuppressWarnings("SqlNoDataSourceInspection")
@Repository
public class TemperatureRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  private static final ZoneId TIMEZONE = ZoneId.of("Europe/Oslo");
  private RowMapper<TemperatureReading> rowMapper =
      (rs, rowNum) -> new TemperatureReading(
          toLocalDateTime(rs.getTimestamp("datetime")),
          rs.getDouble("celsius")
      );

  public TemperatureRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void storeReading(TemperatureReading reading) {
    String query = "INSERT INTO temperature(datetime, celsius) VALUES (:datetime, :celsius)";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("datetime", toSqlTimestamp(reading.getTimestamp()), Types.TIMESTAMP);
    params.addValue("celsius", reading.getCelsius(), Types.DOUBLE);

    jdbcTemplate.update(query, params);
  }

  public List<TemperatureReading> fetchAllTemperatures() {
    String query = "SELECT datetime, celsius FROM temperature";

    return jdbcTemplate.query(query, rowMapper);
  }

  public List<TemperatureReading> fetchTemperaturesBetween(LocalDateTime from, LocalDateTime to) {
    String query = "SELECT datetime, celsius FROM temperature WHERE datetime BETWEEN :from AND :to";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("from", toSqlTimestamp(from), Types.TIMESTAMP);
    params.addValue("to", toSqlTimestamp(to), Types.TIMESTAMP);

    return jdbcTemplate.query(query, params, rowMapper);
  }

  private Timestamp toSqlTimestamp(LocalDateTime dateTime) {
    return Timestamp.valueOf(dateTime);
  }

  private LocalDateTime toLocalDateTime(Timestamp timestamp) {
    return timestamp.toLocalDateTime();
  }
}
