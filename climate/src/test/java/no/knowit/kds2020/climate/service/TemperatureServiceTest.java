package no.knowit.kds2020.climate.service;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import no.knowit.kds2020.climate.db.TemperatureRepository;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.junit.Test;

public class TemperatureServiceTest {

  private static final ZoneId OSLO_ZONE = ZoneId.of("Europe/Oslo");
  private static final LocalDateTime NOW =
      LocalDateTime.of(2020, 1, 18, 9, 0, 0);
  private static final Clock FIXED_CLOCK =
      Clock.fixed(NOW.atZone(OSLO_ZONE).toInstant(), OSLO_ZONE);

  private TemperatureConverter converterMock = mock(TemperatureConverter.class);
  private TemperatureRepository repositoryMock = mock(TemperatureRepository.class);
  private TemperatureService service =
//      new TemperatureService(converterMock, repositoryMock);
      new TemperatureService(FIXED_CLOCK, converterMock, repositoryMock);

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

  @Test
  public void storeCurrentTemperatureFromFahrenheit_should_convert_then_store() {
    when(converterMock.fahrenheitToCelsius(anyDouble()))
        .thenReturn(10.0);

    service.storeCurrentTemperatureFromFahrenheit(100.0);

    verify(repositoryMock).storeReading(argThat(reading -> reading.getCelsius() == 10.0));
  }

  @Test
  public void use_fixed_clock_for_time_comparison() {
    service.storeCurrentTemperature(5);

    verify(repositoryMock).storeReading(new TemperatureReading(NOW, 5));
  }

}
