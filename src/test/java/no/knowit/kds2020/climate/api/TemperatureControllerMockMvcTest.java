package no.knowit.kds2020.climate.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import no.knowit.kds2020.climate.model.TemperatureReading;
import no.knowit.kds2020.climate.service.TemperatureService;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TemperatureControllerMockMvcTest {

  private TemperatureService serviceMock = mock(TemperatureService.class);
  private TemperatureController controller = new TemperatureController(serviceMock);
  private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

  @Test
  public void getAllTemperatureReadings() throws Exception {
    LocalDateTime timestamp = LocalDateTime.of(2020, 1, 11, 20, 50, 17, 0);
    List<TemperatureReading> expectedReadings = Collections.singletonList(
        new TemperatureReading(timestamp, 25.0)
    );

    when(serviceMock.getAllTemperatureReadings())
        .thenReturn(expectedReadings);

    mockMvc.perform(
        get("/temperature/readings/all")
    )
        .andDo(print())
        .andExpect(content().json(
            "[{\"timestamp\": \"2020-01-11T20:50:17\", \"celsius\": 25.0}]"
        ));
  }
}
