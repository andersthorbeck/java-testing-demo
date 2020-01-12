package no.knowit.kds2020.climate.service;

import static no.knowit.kds2020.climate.service.TemperatureConverter.celsiusToFahrenheit;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemperatureConverterTest {

  private static final double ACCEPTABLE_DELTA = 0.0001;

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value() {
    assertEquals(32.0, celsiusToFahrenheit(0), ACCEPTABLE_DELTA);
    assertEquals(212.0, celsiusToFahrenheit(100), ACCEPTABLE_DELTA);
    assertEquals(-148.0, celsiusToFahrenheit(-100), ACCEPTABLE_DELTA);
  }
}
