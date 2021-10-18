package doo.daba.java.metro;

import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.LineEnum;
import doo.daba.java.metro.model.RouteStep;
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
    // Verifica que para las lineas 2 y 8 contengan dos estaciones de enlace (considerar que el indice comienza en 0)
    assertEquals(commonStations.size(), 2);
  }

  @Test
  @DisplayName("Verifica que se genere una Linea indicando el recorrido de estaciones")
  public void testSublistStations() {
    List<Line> lines = this.parser.extractAll();
    int lineToTest = 1;
    int initialIndex = 3;
    int finalIndex = 8;
    String initialStationName = lines.get(lineToTest).getStations().get(initialIndex).getName();
    String finalStationName = lines.get(lineToTest).getStations().get(finalIndex).getName();
    Line route = RouteFinder.lineSublist(initialStationName, finalStationName, LineEnum.LINEA_2, lines);
    // Se verifica que la primer estación de la ruta calculada sea la estación inicial
    assertEquals(initialStationName, route.getStations().get(0).getName());
    // Se verificamos que la ultima estación de la ruta calculada sea la estacion destino
    assertEquals(finalStationName, route.getStations().get(route.getStations().size() - 1).getName());
  }

  @Test
  @DisplayName("Verifica que se encuentre una estacion dentro de una linea")
  public void testStationIndex() {
    int stationIndex = 5;
    Line line = this.parser.extractAll().get(1);
    String stationName = line.getStations().get(stationIndex).getName();
    int stationIndexFound = RouteFinder.findStationIndex(stationName, line.getStations());
    // Se verifica que el indice de la estación que encontramos corresponda con la que se envia a preba
    assertEquals(stationIndex, stationIndexFound);
  }

  @Test
  @DisplayName("Busca una ruta a partir de estaciones de diferentes lineas")
  public void testFindSingleRoute() {
    String initialStation = "Centro Médico";
    String finalStation = "Ermita";
    List<Line> lines = this.parser.extractAll();
    List<RouteStep> route = RouteFinder.findSingleRoute(initialStation, finalStation, lines);
    assertNotNull(route);
    assertEquals(route.isEmpty(), false);
    // Se verifica que la primer estación de la ruta calculada sea la estación inicial
    assertEquals(route.get(0).getInitial().getName(), initialStation);
    //assertEquals(route.get(0).getStations().get(0).getName(), initialStation);
    // Se verificamos que la ultima estación de la ruta calculada sea la estacion destino
    assertEquals(route.get(route.size() - 1).getDestination().getName(),finalStation);
  }

  @Test
  @DisplayName("X")
  public void testFindRoute() {
    String initialStation = "Eugenia";
    String finalStation = "Polanco";
    List<Line> lines = this.parser.extractAll();
    List<RouteStep> route = RouteFinder.findRoute(initialStation, finalStation, lines);
  }
}
