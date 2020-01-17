package no.knowit.kds2020.climate.db;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
//@JdbcTest
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
public class TemperatureRepositoryTestWithInMemoryDb {

  // TODO: Figure out how to manually specify H2 config?

  @Autowired
  TemperatureRepository repository;

//  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//  @Autowired
//  DataSource dataSource;

  @Test
  @FlywayTest(locationsForMigrate = "seed")
  public void fetchAllTemperatures_should_fetch_expected_values_from_db() {
//    cleanAndMigrateDbWithSeed();

    List<TemperatureReading> readings = repository.fetchAllTemperatures();

    TemperatureReading expectedReading =
        new TemperatureReading(LocalDateTime.of(2020, 1, 11, 12, 10, 0), 20.0);
    assertThat(readings, is(equalTo(singletonList(expectedReading))));
  }

/*
  private void cleanAndMigrateDbWithSeed() {
    Flyway flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("db/migration", "seed")
        .load();

    flyway.migrate();
  }
*/

}
