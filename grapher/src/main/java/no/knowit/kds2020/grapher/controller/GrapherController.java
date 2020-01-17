package no.knowit.kds2020.grapher.controller;

import no.knowit.kds2020.grapher.service.GrapherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrapherController {

  private final GrapherService service;

  @Autowired
  public GrapherController(GrapherService service) {
    this.service = service;
  }

  @GetMapping("/graph")
  public String drawTemperatureGraph() {
    String graph = service.generateTemperatureGraph();

    return graph;
  }

}
