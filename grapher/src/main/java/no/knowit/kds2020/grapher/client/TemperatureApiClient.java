package no.knowit.kds2020.grapher.client;

import java.util.List;
import no.knowit.kds2020.grapher.model.TemperatureReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TemperatureApiClient {

  private static final String apiUrl = "http://localhost:8080/temperature/readings/all";

  private final RestTemplate restTemplate;

  @Autowired
  public TemperatureApiClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void getAllTemperatureReadings() {
    ResponseEntity<List<TemperatureReading>> responseEntity = restTemplate.exchange(
        apiUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<TemperatureReading>>() {}
    );

    List<TemperatureReading> readings = responseEntity.getBody();

    System.out.println(readings);
  }

}
