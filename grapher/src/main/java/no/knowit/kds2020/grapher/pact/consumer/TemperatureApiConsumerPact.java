package no.knowit.kds2020.grapher.pact.consumer;

import static org.springframework.http.HttpMethod.GET;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import java.util.List;
import no.knowit.kds2020.grapher.client.TemperatureApiClient;
import no.knowit.kds2020.grapher.model.TemperatureReading;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class TemperatureApiConsumerPact {

  private static final String CONSUMER_NAME = "grapher";
  private static final String PROVIDER_NAME = "climate";

  private static final String MOCK_SERVER_HOST = "localhost";
  private static final int MOCK_SERVER_PORT = 1234;
  private static final String MOCK_SERVER_URL =
      "http://" + MOCK_SERVER_HOST + ":" + MOCK_SERVER_PORT;

  private TemperatureApiClient client = new TemperatureApiClient(
      MOCK_SERVER_URL, new RestTemplate());

  @Rule
  public PactProviderRule mockProvider =
      new PactProviderRule(PROVIDER_NAME, MOCK_SERVER_HOST, MOCK_SERVER_PORT, this);

  @Pact(provider = PROVIDER_NAME, consumer = CONSUMER_NAME)
  public RequestResponsePact createPact(PactDslWithProvider builder) {
    return builder
        .given("there exist a few temperature readings")
        .uponReceiving("a request for all temperature readings")
          .path("/temperature/readings/all")
          .method(GET.toString())
        .willRespondWith()
          .status(200)
          .body(LambdaDsl.newJsonArray(readings -> {
            readings.object(reading -> {
              reading.timestamp("timestamp");
              reading.numberType("celsius", 13);
            });
          }).build())
        .toPact();
  }

  @Test
  @PactVerification(PROVIDER_NAME)
  public void runTest() {
    List<TemperatureReading> readings = client.getAllTemperatureReadings();
    System.out.println("Readings: " + readings);
  }

}
