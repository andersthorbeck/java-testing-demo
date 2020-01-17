package no.knowit.kds2020.climate.db;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.sql.DataSource;
import no.knowit.kds2020.climate.Application;
import no.knowit.kds2020.climate.db.TemperatureRepositoryTestWithContainerizedDb.LocalConfig;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
@ContextConfiguration(classes = LocalConfig.class)
public class TemperatureRepositoryTestWithContainerizedDb {

  private static final ZoneId OSLO_ZONE = ZoneId.of("Europe/Oslo");
  private static final LocalDateTime NOW = LocalDateTime.of(2020, 1, 18, 9, 30, 0);
  private static final Clock FIXED_CLOCK =
      Clock.fixed(NOW.atZone(OSLO_ZONE).toInstant(), OSLO_ZONE);

  @ClassRule
  public static PostgreSQLContainer postgres = new PostgreSQLContainer();

  @Configuration
  static class LocalConfig {
    @Bean
    public DataSource dataSource() {
      return DataSourceBuilder.create()
          .url(postgres.getJdbcUrl())
          .username(postgres.getUsername())
          .password(postgres.getPassword())
          .build();
    }
  }

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // https://stackoverflow.com/q/26889970/854151
  @Autowired
  NamedParameterJdbcTemplate jdbcTemplate;

  TestRepository testRepository;
  TemperatureRepository repository;

  @Before
  @FlywayTest
  public void setUp() {
    testRepository = new TestRepository(jdbcTemplate, FIXED_CLOCK);
    repository = new TemperatureRepository(jdbcTemplate);
  }

  @Test
  public void fetchAllTemperatures_should_fetch_expected_values_from_db() {
    TemperatureReading expectedReading =
        new TemperatureReading(LocalDateTime.of(2020, 1, 11, 12, 10, 0), 20.0);

    testRepository.insertRow(expectedReading);

    List<TemperatureReading> actualReadings = repository.fetchAllTemperatures();

    assertThat(actualReadings, is(equalTo(singletonList(expectedReading))));
  }

}
