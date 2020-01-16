package no.knowit.kds2020.climate.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemperatureConverterTest {

  private static final double ACCEPTABLE_DELTA = 0.0001;

  private TemperatureConverter converter = new TemperatureConverter();

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value() {
    assertEquals(32.0, converter.celsiusToFahrenheit(0), ACCEPTABLE_DELTA);
    assertEquals(212.0, converter.celsiusToFahrenheit(100), ACCEPTABLE_DELTA);
    assertEquals(-148.0, converter.celsiusToFahrenheit(-100), ACCEPTABLE_DELTA);
  }

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value_with_hamcrest() {
    assertThat(converter.celsiusToFahrenheit(0), is(equalTo(32.0)));
    assertThat(converter.celsiusToFahrenheit(100), is(equalTo(212.0)));
    assertThat(converter.celsiusToFahrenheit(-100), is(equalTo(-148.0)));
  }

}
