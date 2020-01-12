package no.knowit.kds2020.grapher.service;

import java.util.List;
import no.knowit.kds2020.grapher.client.TemperatureApiClient;
import no.knowit.kds2020.grapher.model.TemperatureReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrapherService {

  private final TemperatureApiClient client;

  @Autowired
  public GrapherService(TemperatureApiClient client) {
    this.client = client;
  }

  public String generateTemperatureGraph() {
    List<TemperatureReading> readings = client.getAllTemperatureReadings();

    return readings.toString();
  }
}
