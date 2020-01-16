package no.knowit.kds2020.climate.db;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
@Ignore("requires a running DB reachable via the datasource config")
public class TemperatureRepositoryTestWithExternalDb {

  @Autowired
  TemperatureRepository repository;

  /**
   * Start temporary external containerized database with:
   *   $ docker run -u postgres -p 5432:5432 -e POSTGRES_PASSWORD=password postgres:9
   * If required, open psql REPL to run manual SQL against that database with:
   *   $ psql -h localhost -U postgres
   */

  @Test
  @FlywayTest(locationsForMigrate = "seed")
  public void test() {
    List<TemperatureReading> readings = repository.fetchAllTemperatures();

    TemperatureReading expectedReading =
        new TemperatureReading(LocalDateTime.of(2020, 1, 11, 12, 10, 0), 20.0);
    assertThat(readings, is(equalTo(singletonList(expectedReading))));
  }

}
