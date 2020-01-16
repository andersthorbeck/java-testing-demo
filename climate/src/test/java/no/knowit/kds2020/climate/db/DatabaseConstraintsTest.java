package no.knowit.kds2020.climate.db;

import static no.knowit.kds2020.climate.db.TemperatureRepository.toSqlTimestamp;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
@ActiveProfiles("db-test")
public class DatabaseConstraintsTest {

  private static final LocalDateTime NOW_DATETIME = LocalDateTime.of(2020, 1, 18, 9, 30, 0);
  private static final Timestamp NOW_TIMESTAMP = toSqlTimestamp(NOW_DATETIME);
  private static final double DEFAULT_CELSIUS = 10.0;

  @ClassRule
  public static PostgreSQLContainer postgres = new PostgreSQLContainer();

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // https://stackoverflow.com/q/26889970/854151
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Before
  @FlywayTest  // Reset DB state before each test, to ensure they are independent.
  public void setUp() {
  }

  private void performInsert(MapSqlParameterSource params) {
    String sql = "INSERT INTO temperature(datetime, celsius) VALUES (:datetime, :celsius)";

    jdbcTemplate.update(sql, params);
  }

  @NotNull
  private MapSqlParameterSource createDefaultParameters() {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("datetime", NOW_TIMESTAMP, Types.TIMESTAMP);
    params.addValue("celsius", DEFAULT_CELSIUS, Types.DOUBLE);
    return params;
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void datetime_is_not_nullable() {
    MapSqlParameterSource params = createDefaultParameters();
    params.addValue("datetime", null, Types.TIMESTAMP);
    performInsert(params);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void celsius_is_not_nullable() {
    MapSqlParameterSource params = createDefaultParameters();
    params.addValue("celsius", null, Types.DOUBLE);
    performInsert(params);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void datetime_must_be_timestamp() {
    MapSqlParameterSource params = createDefaultParameters();
    params.addValue("datetime", "noon today", Types.TIMESTAMP);
    performInsert(params);
  }

  @Test(expected = BadSqlGrammarException.class)
  public void celsius_must_be_number() {
    MapSqlParameterSource params = createDefaultParameters();
    params.addValue("celsius", "twenty", Types.DOUBLE);
    performInsert(params);
  }

  @Test(expected = DuplicateKeyException.class)
  public void datetime_must_be_unique() {
    MapSqlParameterSource params = createDefaultParameters();
    performInsert(params);
    params.addValue("celsius", DEFAULT_CELSIUS + 1, Types.DOUBLE);
    performInsert(params);
  }

  @Test
  public void celsius_does_not_need_to_be_unique() {
    MapSqlParameterSource params = createDefaultParameters();
    performInsert(params);
    Timestamp laterTime = toSqlTimestamp(NOW_DATETIME.plusMinutes(1));
    params.addValue("datetime", laterTime, Types.TIMESTAMP);
    performInsert(params);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void celsius_can_not_be_below_absolute_zero() {
    MapSqlParameterSource params = createDefaultParameters();
    params.addValue("celsius", -274, Types.DOUBLE);
    performInsert(params);
  }

  @Test
  public void celsius_can_be_just_above_absolute_zero() {
    MapSqlParameterSource params = createDefaultParameters();
    params.addValue("celsius", -273, Types.DOUBLE);
    performInsert(params);
  }

}
