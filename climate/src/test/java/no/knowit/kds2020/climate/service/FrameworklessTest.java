package no.knowit.kds2020.climate.service;

public class FrameworklessTest {

  public static void main(String[] args) {
    testCelsiusToFahrenheit();
    testCelsiusToFahrenheitWithoutAssert();
  }

  /** Be careful, java assertions must be explicitly enabled (by specifying JVM flag -ea),
    * or they will not be run. */
  private static void testCelsiusToFahrenheit() {
    TemperatureConverter converter = new TemperatureConverter();
    assert converter.celsiusToFahrenheit(0) == 32.0 : "Actual value did not match expected value";
  }

  private static void testCelsiusToFahrenheitWithoutAssert() {
    TemperatureConverter converter = new TemperatureConverter();
    if (converter.celsiusToFahrenheit(0) != 32.0) {
      throw new RuntimeException("Actual value did not match expected value");
    }
    System.out.println("Test passed!");
  }

}
