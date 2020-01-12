package no.knowit.kds2020.climate.db;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import no.knowit.kds2020.climate.Application;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
public class TemperatureRepositoryTestWithExternalDb {

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
