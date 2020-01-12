package no.knowit.kds2020.grapher.controller;

import java.util.List;
import no.knowit.kds2020.grapher.client.TemperatureApiClient;
import no.knowit.kds2020.grapher.model.TemperatureReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrapherController {

  private final TemperatureApiClient client;

  @Autowired
  public GrapherController(TemperatureApiClient client) {
    this.client = client;
  }

  @GetMapping("/graph")
  public void drawTemperatureGraph() {
    List<TemperatureReading> readings = client.getAllTemperatureReadings();

    System.out.println(readings);
  }

}
