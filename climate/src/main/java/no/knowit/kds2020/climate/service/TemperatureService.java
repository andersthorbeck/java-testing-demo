package no.knowit.kds2020.climate.service;

import java.time.LocalDateTime;
import java.util.List;
import no.knowit.kds2020.climate.db.TemperatureRepository;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {

  private final TemperatureConverter converter;
  private final TemperatureRepository repository;

  @Autowired
  public TemperatureService(
      TemperatureConverter converter,
      TemperatureRepository repository
  ) {
    this.converter = converter;
    this.repository = repository;
  }

  public List<TemperatureReading> getAllTemperatureReadings() {
    return repository.fetchAllTemperatures();
  }

  public void storeCurrentTemperature(double celsius) {
    repository.storeReading(new TemperatureReading(LocalDateTime.now(), celsius));
  }

  public void storeCurrentTemperatureFromFahrenheit(double fahrenheit) {
    storeCurrentTemperature(converter.fahrenheitToCelsius(fahrenheit));
  }
}
