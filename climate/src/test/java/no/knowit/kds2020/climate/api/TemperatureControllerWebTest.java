package no.knowit.kds2020.climate.api;

import static org.mockito.Mockito.verify;

import java.util.List;
import no.knowit.kds2020.climate.db.TemperatureRepository;
import no.knowit.kds2020.climate.model.TemperatureReading;
import no.knowit.kds2020.climate.service.TemperatureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class TemperatureControllerWebTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private TemperatureService serviceMock;

  /** Note: should ideally not have to explicitly mock the repository layer,
    * when already mocking the service layer. */
  @MockBean
  private TemperatureRepository repositoryMock;

  @Test
  public void get_against_temperature_readings_all_path_should_call_getAllTemperatureReadings() {
    //noinspection unchecked
    List<TemperatureReading> readings =
        restTemplate.getForObject("/temperature/readings/all", List.class);

    verify(serviceMock).getAllTemperatureReadings();
  }
}
