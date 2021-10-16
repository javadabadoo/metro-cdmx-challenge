package doo.daba.java.metro;

import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.Station;
import doo.daba.java.metro.parser.MetroParser;
import doo.daba.java.metro.util.RouteFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UtilTest {

  private final MetroParser parser = new MetroParser("Metro_CDMX.kml");

  @ParameterizedTest
  @CsvSource(
      {
          "Centro Médico,2",
          "Pantitlán,4",
          "Zapata,2",
          "Martin Carrera,2"
      })
  @DisplayName("Verifica algunas lineas con cambio de estación")
  public void testConnectionExtractor(String stationName, int expectedLines) {
    List<Line> lines = this.parser.extractAll();
    Map<String, Station> connections = RouteFinder.findConnections(lines);
    assertNotNull(connections.get(stationName));
    assert connections.get(stationName).getLines().size() == expectedLines;
  }

  @Test
  @DisplayName("Verifica que encuentre las lineas de alguna estación")
  public void testLinesFinder() {
    List<Line> lines = this.parser.extractAll();
    lines.forEach(line -> line.getStations().forEach(station -> {
      assert null == station.getName()
          || RouteFinder.findLineByStationName(station.getName(), lines).contains(line.getName());
    }));
  }


  @Test
  @DisplayName("Verifica que encuentre las lineas de alguna estación")
  public void testCoomonStations() {
    List<Line> lines = this.parser.extractAll();
    List<Station> commonStations = RouteFinder.findCommonStations(lines.get(1), lines.get(7));
    assertEquals(commonStations.size(), 2);
  }
}
