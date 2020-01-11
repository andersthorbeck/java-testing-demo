package no.knowit.kds2020.climate.db;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import no.knowit.kds2020.climate.Application;
import no.knowit.kds2020.climate.db.TemperatureRepositoryTestWithContainerizedDb.LocalConfig;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
@ContextConfiguration(classes = LocalConfig.class)
public class TemperatureRepositoryTestWithContainerizedDb {

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

  @Autowired
  TemperatureRepository repository;

  @Test
  @FlywayTest(locationsForMigrate = "seed")
  public void test() {
    List<TemperatureReading> readings = repository.fetchAllTemperatures();

    TemperatureReading expectedReading =
        new TemperatureReading(LocalDateTime.of(2020, 1, 11, 12, 10, 0), 20.0);
    assertThat(readings, is(equalTo(singletonList(expectedReading))));
  }

}
