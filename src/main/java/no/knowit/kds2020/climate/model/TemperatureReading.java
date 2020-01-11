package no.knowit.kds2020.climate.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemperatureReading that = (TemperatureReading) o;
    return Double.compare(that.celsius, celsius) == 0
        && Objects.equals(timestamp, that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, celsius);
  }

  @Override
  public String toString() {
    return "TemperatureReading{"
        + "timestamp=" + timestamp
        + ", celsius=" + celsius
        + '}';
  }
}
