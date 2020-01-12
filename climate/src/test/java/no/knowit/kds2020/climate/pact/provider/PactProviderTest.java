package no.knowit.kds2020.climate.pact.provider;

import static no.knowit.kds2020.climate.pact.provider.PactProviderTest.PROVIDER_NAME;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import java.time.LocalDateTime;
import java.util.Arrays;
import no.knowit.kds2020.climate.model.TemperatureReading;
import no.knowit.kds2020.climate.service.TemperatureService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRestPactRunner.class)
@WebMvcTest
@Provider(PROVIDER_NAME)
@PactFolder("pacts")
public class PactProviderTest {

  private static final String CONSUMER_NAME = "grapher";
  static final String PROVIDER_NAME = "climate";

  private static final String MOCK_SERVER_HOST = "localhost";
  private static final int MOCK_SERVER_PORT = 1234;
  private static final String MOCK_SERVER_URL =
      "http://" + MOCK_SERVER_HOST + ":" + MOCK_SERVER_PORT;

  @MockBean
  private TemperatureService serviceMock;

  @TestTarget
  Target pactTestTarget = new HttpTarget(MOCK_SERVER_PORT);

  @State("there exist a few temperature readings")
  public void prepareTemperatureReadings() {
    LocalDateTime now = LocalDateTime.now();
    when(serviceMock.getAllTemperatureReadings())
        .thenReturn(Arrays.asList(
            new TemperatureReading(now.minusMinutes(5), 15),
            new TemperatureReading(now, 22)
        ));
  }

}
