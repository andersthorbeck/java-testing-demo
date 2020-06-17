package no.knowit.kds2020.climate.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemperatureConverterTest {

  private TemperatureConverter converter = new TemperatureConverter();

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value() {
    double acceptableDelta = 0.0001;  // The doubles cannot deviate by more than this
    assertEquals(32.0, converter.celsiusToFahrenheit(0), acceptableDelta);
    assertEquals(212.0, converter.celsiusToFahrenheit(100), acceptableDelta);
    assertEquals(-148.0, converter.celsiusToFahrenheit(-100), acceptableDelta);
  }

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value_with_hamcrest() {
    assertThat(converter.celsiusToFahrenheit(0), is(equalTo(32.0)));
    assertThat(converter.celsiusToFahrenheit(100), is(equalTo(212.0)));
    assertThat(converter.celsiusToFahrenheit(-100), is(equalTo(-148.0)));
  }

}
