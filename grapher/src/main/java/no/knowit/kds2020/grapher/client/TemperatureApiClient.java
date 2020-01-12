package no.knowit.kds2020.grapher.client;

import java.util.List;
import no.knowit.kds2020.grapher.model.TemperatureReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TemperatureApiClient {

  private final String apiBaseUrl;
  private final RestTemplate restTemplate;

  @Autowired
  public TemperatureApiClient(
      @Value("http://localhost:8080") String apiBaseUrl,
      RestTemplate restTemplate
  ) {
    this.restTemplate = restTemplate;
    this.apiBaseUrl = apiBaseUrl;
  }

  public List<TemperatureReading> getAllTemperatureReadings() {
    ResponseEntity<List<TemperatureReading>> responseEntity = restTemplate.exchange(
        apiBaseUrl + "/temperature/readings/all",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<TemperatureReading>>() {}
    );

    List<TemperatureReading> readings = responseEntity.getBody();

    return readings;
  }

}
