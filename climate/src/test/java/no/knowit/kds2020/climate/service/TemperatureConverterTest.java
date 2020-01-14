package no.knowit.kds2020.climate.service;

import static no.knowit.kds2020.climate.service.TemperatureConverter.celsiusToFahrenheit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TemperatureConverterTest {

  private static final double ACCEPTABLE_DELTA = 0.0001;

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value() {
    assertEquals(32.0, celsiusToFahrenheit(0), ACCEPTABLE_DELTA);
    assertEquals(212.0, celsiusToFahrenheit(100), ACCEPTABLE_DELTA);
    assertEquals(-148.0, celsiusToFahrenheit(-100), ACCEPTABLE_DELTA);
  }

  @Test
  public void demonstrate_junit_assertions() {
    assertEquals("foo", "foo");
    assertNotEquals("foo", "bar");

    Object object = new Object();
    assertSame(object, object);
    assertNotSame(object, new Object());

    assertNull(null);
    assertNotNull(object);

    assertTrue(0 < 1);
    assertFalse(2 < 1);

    assertThrows(Exception.class, () -> {throw new Exception();});
  }

  @Test
  public void celsiusToFahrenheit_should_convert_to_expected_fahrenheit_value_with_hamcrest() {
    assertThat(celsiusToFahrenheit(0), is(equalTo(32.0)));
    assertThat(celsiusToFahrenheit(100), is(equalTo(212.0)));
    assertThat(celsiusToFahrenheit(-100), is(equalTo(-148.0)));
  }

  @Test
  public void demonstrate_hamcrest_matchers() {
    assertThat("foo", equalTo("foo"));
    assertThat("foo", is(equalTo("foo")));
    assertThat("foo", is("foo"));

    assertThat("foo", is(not(equalTo("bar"))));

    Object object = new Object();
    assertThat(object, is(sameInstance(object)));
    assertThat(object, is(not(sameInstance(new Object()))));

    assertThat(null, is(nullValue()));
    assertThat(object, is(not(nullValue())));

    assertThat(0 < 1, is(true));
    assertThat(2 < 1, is(false));

    // Use JUnit assertThrows for exceptions
  }

}
