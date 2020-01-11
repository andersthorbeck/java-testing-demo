package no.knowit.kds2020.climate.service;

import java.util.List;
import no.knowit.kds2020.climate.db.TemperatureRepository;
import no.knowit.kds2020.climate.model.TemperatureReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {

  private final TemperatureRepository repository;

  @Autowired
  public TemperatureService(TemperatureRepository repository) {
    this.repository = repository;
  }

  public List<TemperatureReading> getAllTemperatureReadings() {
    return repository.fetchAllTemperatures();
  }
}
