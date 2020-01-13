package no.knowit.kds2020.climate.pact.provider;

import static no.knowit.kds2020.climate.pact.provider.PactProviderTest.PROVIDER_NAME;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import java.time.LocalDateTime;
import java.util.Arrays;
import no.knowit.kds2020.climate.api.TemperatureController;
import no.knowit.kds2020.climate.model.TemperatureReading;
import no.knowit.kds2020.climate.service.TemperatureService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRestPactRunner.class)
@Provider(PROVIDER_NAME)
@PactFolder("pacts")
@WebMvcTest
public class PactProviderTest {

  static final String PROVIDER_NAME = "climate";

  @MockBean
  private TemperatureService serviceMock;

  @Autowired
  private TemperatureController controller;

  @TestTarget
  public MockMvcTarget pactTestTarget = new MockMvcTarget();

  @Before
  public void setUp() {
    pactTestTarget.setControllers(controller);
    pactTestTarget.setPrintRequestResponse(true);
  }

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
