package no.knowit.kds2020.climate.endtoend;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
    listeners = FlywayTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS
)
@ActiveProfiles("db-test")
public class TemperatureControllerEndToEndTest {

  @ClassRule
  public static PostgreSQLContainer postgres = new PostgreSQLContainer();

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @FlywayTest(locationsForMigrate = "seed")
  public void getAllTemperatureReadings_should_fetch_from_db_and_return_expected_http_response() {
    ResponseEntity<List<TemperatureReading>> responseEntity = restTemplate.exchange(
        "/temperature/readings/all",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<TemperatureReading>>(){});

    List<TemperatureReading> readings = responseEntity.getBody();

    List<TemperatureReading> expectedReadings = singletonList(
        new TemperatureReading(LocalDateTime.parse("2020-01-11T12:10:00"), 20.0)
    );
    assertThat(readings, is(equalTo(expectedReadings)));
  }

}
