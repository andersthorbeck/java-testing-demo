package no.knowit.kds2020.climate.api;

import static org.mockito.Mockito.verify;

import java.util.List;
import javax.sql.DataSource;
import no.knowit.kds2020.climate.model.TemperatureReading;
import no.knowit.kds2020.climate.service.TemperatureService;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TemperatureControllerWebTest {

  /** Note: shouldn't need this, but couldn't figure out how to
    * only load top layer of application context. */
  @ClassRule
  public static PostgreSQLContainer postgres = new PostgreSQLContainer();

  @TestConfiguration
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
  private TestRestTemplate restTemplate;

  @MockBean
  private TemperatureService serviceMock;

  @Test
  public void get_against_temperature_readings_all_path_should_call_getAllTemperatureReadings() {
    //noinspection unchecked
    List<TemperatureReading> readings =
        restTemplate.getForObject("/temperature/readings/all", List.class);

    verify(serviceMock).getAllTemperatureReadings();
  }
}
