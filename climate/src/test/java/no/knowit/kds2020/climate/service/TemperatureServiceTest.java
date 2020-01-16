package no.knowit.kds2020.climate.service;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import no.knowit.kds2020.climate.db.TemperatureRepository;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.junit.Test;

public class TemperatureServiceTest {

  private TemperatureRepository repositoryMock = mock(TemperatureRepository.class);
  private TemperatureService service = new TemperatureService(repositoryMock);

  @Test
  public void getAllTemperatureReadings_should_fetch_from_database() {
    LocalDateTime now = LocalDateTime.now();
    List<TemperatureReading> expectedReadings =
        singletonList(new TemperatureReading(now, 13));

    when(repositoryMock.fetchAllTemperatures())
        .thenReturn(expectedReadings);

    List<TemperatureReading> actualReadings = service.getAllTemperatureReadings();

    assertThat(actualReadings, is(equalTo(expectedReadings)));
  }

  @Test
  public void storeCurrentTemperature_should_delegate_to_database() {
    service.storeCurrentTemperature(5);

    verify(repositoryMock).storeReading(any(TemperatureReading.class));
  }

}
