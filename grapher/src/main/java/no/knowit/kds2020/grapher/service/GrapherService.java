package no.knowit.kds2020.grapher.service;

import static java.util.Comparator.reverseOrder;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    if (readings.isEmpty()) {
      return "No readings";
    }

    List<Double> allTemperatures =
        readings.stream()
            .map(TemperatureReading::getCelsius)
            .collect(Collectors.toList());

    int minTemp = allTemperatures.stream().sorted().findFirst().map(Math::round).map(Math::toIntExact).get();
    int maxTemp = allTemperatures.stream().sorted(reverseOrder()).findFirst().map(Math::round).map(Math::toIntExact).get();
    int maxDigits = Math.max(numDigits(maxTemp), numDigits(minTemp));

    boolean positiveAndNegative = Math.abs(Math.signum(maxTemp) - Math.signum(minTemp)) > 1;
    int extraRows = positiveAndNegative ? 3 : 2;
    int columnTotalWidth = maxDigits % 2 == 1 ? maxDigits : maxDigits + 1;
    int columnSpace = (columnTotalWidth - 1) / 2;

    StringBuilder graphBuilder = new StringBuilder();
    // t is thermometer level, r is temperature reading
    for (int t = maxTemp + 1; t >= minTemp - 1; t--) {
      graphBuilder.append(leftPad(t, maxDigits, ' '));
      graphBuilder.append(t == 0 ? '+' : '|');
      int finalT = t;
      allTemperatures.stream().map(Math::round).map(Math::toIntExact).forEach(
          new Consumer<Integer>() {
            @Override
            public void accept(Integer r) {
              char padChar = finalT == 0 ? '-' : ' ';
              if (finalT == r + nonZeroSignum(r)) {
                graphBuilder.append(centerPad(r, columnTotalWidth, padChar));
              } else if (finalT == r) {
                graphBuilder
                    .append(repeat(' ', columnSpace))
                    .append('*')
                    .append(repeat(' ', columnSpace));
              } else if (finalT == 0) {
                graphBuilder.append(repeat('-', columnTotalWidth));
              } else if (Math.abs(r) <= Math.abs(finalT)) {
                graphBuilder
                    .append(repeat(' ', columnSpace))
                    .append('|')
                    .append(repeat(' ', columnSpace));
              } else {
                graphBuilder.append(repeat(' ', columnTotalWidth));
              }
            }
          }
      );
      graphBuilder.append('\n');
    }

    return graphBuilder.toString();
  }

  /** Includes negation sign and decimal sign if present. */
  int numDigits(int n) {
    return String.valueOf(n).length();
  }

  private String leftPad(int num, int totalChars, char padChar) {
    return String.format("%" + totalChars + "s", num).replaceAll(" ", String.valueOf(padChar));
  }

  private String centerPad(int num, int totalChars, char padChar) {
    int numPadChars = totalChars - numDigits(num);
    if (numPadChars <= 0) {
      return String.valueOf(num);
    }
    int numLeftPad = numPadChars / 2;
    int numRightPad = numPadChars - numLeftPad;
    return repeat(padChar, numLeftPad) + String.valueOf(num) + repeat(padChar, numRightPad);
  }

  private String repeat(char ch, int numRepetitions) {
    return IntStream.range(0, numRepetitions).mapToObj(i -> String.valueOf(ch)).collect(Collectors.joining(""));
  }

  private int nonZeroSignum(double n) {
    return n == 0 ? 1 : (int) Math.signum(n);
  }
}
