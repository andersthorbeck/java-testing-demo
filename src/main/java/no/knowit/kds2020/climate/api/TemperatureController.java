package no.knowit.kds2020.climate.api;

import no.knowit.kds2020.climate.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperatureController {

  private final TemperatureService service;

  @Autowired
  public TemperatureController(TemperatureService service) {
    this.service = service;
  }

  @GetMapping("/temperature/readings/all")
  ResponseEntity getAllTemperatureReadings() {
    return ResponseEntity.ok(service.getAllTemperatureReadings());
  }

}
