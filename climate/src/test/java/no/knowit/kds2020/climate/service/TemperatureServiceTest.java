package no.knowit.kds2020.climate.service;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintViolationException;
import no.knowit.kds2020.climate.db.TemperatureRepository;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
  public void storeCurrentTemperatureFromFahrenheit_store_result_of_conversion() {
    when(converterMock.fahrenheitToCelsius(anyDouble()))
        .thenReturn(10.0);

    service.storeCurrentTemperatureFromFahrenheit(100.0);

    verify(repositoryMock).storeReading(argThat(reading -> reading.getCelsius() == 10.0));
  }

  @Test
  public void storeCurrentTemperatureFromFahrenheit_should_convert_then_store() {
    service.storeCurrentTemperatureFromFahrenheit(100.0);

    InOrder inOrder = Mockito.inOrder(converterMock, repositoryMock);
    inOrder.verify(converterMock).fahrenheitToCelsius(anyDouble());
    inOrder.verify(repositoryMock).storeReading(any());
  }

  @Test
  public void use_fixed_clock_for_time_comparison() {
    service.storeCurrentTemperature(5);

    verify(repositoryMock).storeReading(new TemperatureReading(NOW, 5));
  }

  // TODO: Better ArgumentCaptor test
  @Test
  public void storeCurrentRoundedTemperatureFromFahrenheit_should_round_then_convert_then_store() {
    when(converterMock.fahrenheitToCelsius(anyDouble()))
        .thenCallRealMethod();

    service.storeCurrentRoundedTemperatureFromFahrenheit(32.4);

    ArgumentCaptor<TemperatureReading> readingCaptor =
        ArgumentCaptor.forClass(TemperatureReading.class);

    verify(repositoryMock).storeReading(readingCaptor.capture());
    assertThat(readingCaptor.getValue().getCelsius(), is(0.0));
  }

  @Test
  public void storeCurrentTemperatureFromFahrenheit_should_propagate_conversion_exception() {
    when(converterMock.fahrenheitToCelsius(anyDouble()))
        .thenThrow(new IllegalArgumentException());

    assertThrows(IllegalArgumentException.class, () -> {
      service.storeCurrentTemperatureFromFahrenheit(-10000);
    });
  }

  @Test
  public void demo_of_Mockito_Answer() {
    when(converterMock.fahrenheitToCelsius(anyDouble()))
        .thenAnswer((Answer<Double>) (InvocationOnMock invocation) -> {
          System.out.println("Answer for call to "
              + invocation.getMock().getClass().getSimpleName()+ "."
              + invocation.getMethod().getName() + "()"
              + " with arguments "
              + Arrays.asList(invocation.getArguments()));

          double arg0 = invocation.getArgument(0);
          return 2 * arg0;
        });

    service.storeCurrentTemperatureFromFahrenheit(100);

    verify(repositoryMock).storeReading(new TemperatureReading(NOW, 200));
  }

  @Test(expected = ConstraintViolationException.class)
  public void demo_mocking_void_methods() {
//    when(repositoryMock.storeReading(any()))
//        .thenThrow(ConstraintViolationException.class);

    doThrow(ConstraintViolationException.class)
        .when(repositoryMock).storeReading(any());

    service.storeCurrentTemperature(3.0);
  }

  @Test
  public void demo_using_spy_and_doReturn() {
    TemperatureConverter converterSpy = spy(new TemperatureConverter());
    doReturn(999.0)
        .when(converterSpy).fahrenheitToCelsius(anyDouble());

    assertThat(converterSpy.celsiusToFahrenheit(0.0), is(32.0));
    assertThat(converterSpy.fahrenheitToCelsius(32.0), is(999.0));
  }

}
