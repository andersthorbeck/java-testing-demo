package no.knowit.kds2020.climate.db;

import static no.knowit.kds2020.climate.db.TemperatureRepository.toSqlTimestamp;

import java.sql.Types;
import java.time.Clock;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("Duplicates")
public class TestRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final Clock clock;

  public TestRepository(NamedParameterJdbcTemplate jdbcTemplate, Clock clock) {
    this.jdbcTemplate = jdbcTemplate;
    this.clock = clock;
  }

  @Transactional
  public void insertRow(TemperatureReading reading) {
    String query = "INSERT INTO temperature(datetime, celsius) VALUES (:datetime, :celsius)";

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("datetime", toSqlTimestamp(reading.getTimestamp()), Types.TIMESTAMP);
    params.addValue("celsius", reading.getCelsius(), Types.DOUBLE);

    jdbcTemplate.update(query, params);
  }

}
