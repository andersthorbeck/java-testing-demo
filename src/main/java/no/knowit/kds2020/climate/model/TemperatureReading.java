package no.knowit.kds2020.climate.model;

import java.time.LocalDateTime;

public class TemperatureReading {
  private final LocalDateTime timestamp;
  private final double celsius;

  public TemperatureReading(LocalDateTime timestamp, double celsius) {
    this.timestamp = timestamp;
    this.celsius = celsius;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public double getCelsius() {
    return celsius;
  }
}
