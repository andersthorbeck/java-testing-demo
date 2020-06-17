package no.knowit.kds2020.climate.service;

public class FrameworklessTest {

  public static void main(String[] args) {
    testCelsiusToFahrenheit();
  }

  private static void testCelsiusToFahrenheit() {
    TemperatureConverter converter = new TemperatureConverter();
    if (converter.celsiusToFahrenheit(0) != 32.0) {
      throw new RuntimeException("Actual value did not match expected value");
    }
    System.out.println("Test passed!");
  }

}
