package doo.daba.java.metro.util;

import doo.daba.java.metro.exception.RouteException;
import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.LineEnum;
import doo.daba.java.metro.model.RouteStep;
import doo.daba.java.metro.model.Station;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class RouteFinder {
  private RouteFinder() {
  }

  public static Map<String, Station> findConnections(@NonNull final List<LineEnum> lineA,
                                                     @NonNull final List<LineEnum> lineB,
                                                     @NonNull final List<Line> lines) {
    List<Line> linesList = new ArrayList<>();
    List<LineEnum> linesEnumList = new ArrayList<>();
    linesEnumList.addAll(lineA);
    linesEnumList.addAll(lineB);
    List<LineEnum> finalLinesEnumList = linesEnumList.stream().distinct().collect(Collectors.toList());
    lines.forEach(line -> {
      Optional<LineEnum> firstFound = finalLinesEnumList.stream().filter(l -> line.getName().equals(l)).findFirst();
      if (firstFound.isPresent()) linesList.add(line);
    });
    return findConnections(linesList);
  }

  public static Map<String, Station> findConnections(@NonNull final List<Line> lines) {
    Map<String, Station> connections = new HashMap<>();
    lines.forEach(line -> line.getStations().forEach(station -> {
      if (station.getLines().size() > 1) {
        connections.put(station.getName(), station);
      }
    }));
    return connections;
  }

  public static List<LineEnum> findLineByStationName(@NonNull final String name, @NonNull final List<Line> lines) {
    for (Line line : lines) {
      Optional<Station> firstFound = line.getStations().stream().filter(s -> name.equals(s.getName())).findFirst();
      if (firstFound.isPresent()) return firstFound.get().getLines();
    }
    return null;
  }

  public static List<Station> findCommonStations(@NonNull final Line lineA, @NonNull final Line lineB) {
    List<Station> commonStations = new ArrayList<>(lineA.getStations());
    commonStations.retainAll(lineB.getStations());
    return commonStations;
  }

  public static List<RouteStep> findSingleRoute(@NonNull final String from, @NonNull final String to,
                                                @NonNull final List<Line> lines) {
    List<RouteStep> steps = new ArrayList<>();
    List<LineEnum> lineFrom = findLineByStationName(from, lines);
    List<LineEnum> lineTo = findLineByStationName(to, lines);
    if (lineFrom == null || lineTo == null) {
      throw new RouteException("Wow, it seems you're looking for an invalid route.");
    }

    LineEnum commonLine = findCommonLine(lineFrom, lineTo);
    if (commonLine != null) {
      Line line = lineSublist(from, to, commonLine, lines);
      steps.add(
          new RouteStep(Arrays.asList(line))
      );
      return steps.isEmpty() ? null : steps;
    }

    lineFrom.forEach(f -> lineTo.forEach(t -> {
      RouteStep step = new RouteStep(new ArrayList<>());
      List<Station> commonStations = findCommonStations(findLineByEnum(f, lines), findLineByEnum(t, lines));
      commonStations.forEach(c -> step.getLines().add(lineSublist(from, c.getName(), f, lines)));
      commonStations.forEach(c -> step.getLines().add(lineSublist(c.getName(), to, t, lines)));
      if (!step.getLines().isEmpty()) steps.add(step);
    }));
    return steps.isEmpty() ? null : steps;
  }

  public static List<RouteStep> findRoute(@NonNull final String from, @NonNull final String to,
                                     @NonNull final List<Line> lines) {

    List<RouteStep> steps;
    /*
    TODO; Estas listas se calculan en findSingleRoute, por cuestiones de pruebas las dejaré aqui, espero no se me
     olvide pasarlas como parametro
    */
    List<LineEnum> lineFrom = findLineByStationName(from, lines);
    List<LineEnum> lineTo = findLineByStationName(to, lines);

    steps = findSingleRoute(from, to, lines);
    if (steps != null) return steps;

    steps = new ArrayList<>();
    Map<String, Station> connections = findConnections(lineFrom, lineTo, lines);
    for (Map.Entry<String, Station> entry : connections.entrySet()) {
      List<RouteStep> singleRoute = findSingleRoute(entry.getValue().getName(), to, lines);
      if (singleRoute == null) continue;
      for (RouteStep r : singleRoute) {
        if (r.getLines().size() < 2) continue;
          List<RouteStep> lastRoute = findSingleRoute(from, r.getLines().get(0).getStations().get(0).getName(), lines);
          r.getLines().add(0, lastRoute.get(0).getLines().get(0));
        steps.add(r);
      }
    }
    return steps;
  }


  /**
   * Busca si las lineas convergen. Este metodo se utiliza cuando se buscan las lineas de una estación. Debido a que
   * una estación puede tener mas de una linea (estacion de interseccion) se puede buscar que la linea coincda con
   * otra linea proveniente de alguna estacion de intersección.
   * <p>
   * Ejemplo:
   * Jamaica: Corresponde a la linea 4 y a la linea 9
   * Chabacano: Corresponde a la linea 2, linea 8 y linea 9
   * <p>
   * Con el ejemplo anterior se obtiene que la linea en comun es la linea 9. Este dato es utilizado principalmente
   * para calcular la ruta mas simple entre dos estaciones.
   *
   * @param lineFrom Indica la linea destino
   * @param lineTo   Indica la linea final
   * @return Retorna el nombre de la linea en comun indicado por {@link LineEnum}
   */
  private static LineEnum findCommonLine(@NonNull final List<LineEnum> lineFrom,
                                         @NonNull final List<LineEnum> lineTo) {
    for (LineEnum from : lineFrom) {
      Optional<LineEnum> firstFound = lineTo.stream().filter(from::equals).findFirst();
      if (firstFound.isPresent()) return firstFound.get();
    }
    return null;
  }

  /**
   * Obtiene las estaciones que conforman el recorrido entre las estaciones {@code from} y {@code to} dentro de la
   * misma linea.
   * <p>
   * En el caso de que el orden de la lista defina ordenes diferente de acuerdo a sus indices, se invierte la lista
   * de estaciones para que el primer elemento corresponda a la estación definida por {@code from}.
   *
   * @param from  Indica la etsacion de metro a partir de la cual se genera la lista el recorrido
   * @param to    Inidica la estacion de metro hasta la cual se genera la lista de recorrido
   * @param line  Indica la linea de metro a la cual corresponden las estaciones {@code from} y {@code to}
   * @param lines Son las lineas de metro completas a partir de la cual se extraer la lista de estaciones para
   *              generar la lista del recorrido
   * @return Lista que representa el recorrido comprendido entre {@code from} y {@code to}
   */
  public static Line lineSublist(@NonNull final String from, @NonNull final String to, @NonNull final LineEnum line,
                                 @NonNull final List<Line> lines) {

    Optional<Line> firstFound = lines.stream().filter(l -> l.getName().equals(line)).findFirst();
    if (!firstFound.isPresent()) {
      throw new RouteException("WTH! We can't find line when calculating stations for trip.");
    }
    Line foundLine = firstFound.get();
    int initialStationIndex = findStationIndex(from, foundLine.getStations());
    int finalStationIndex = findStationIndex(to, foundLine.getStations());

    if (initialStationIndex < 0 || finalStationIndex < 0) {
      throw new RouteException("Sorry but we couldn't find data for requested station.");
    }

    List<Station> stations = new ArrayList<>(foundLine.getStations());
    if (initialStationIndex > finalStationIndex) {
      stations = stations.subList(finalStationIndex, initialStationIndex + 1);
      Collections.reverse(stations);
    } else {
      stations = stations.subList(initialStationIndex, finalStationIndex + 1);
    }
    return new Line(foundLine.getName(), stations);
  }

  public static int findStationIndex(@NonNull final String station, @NonNull final List<Station> stations) {
    Optional<Station> foundStation = stations.stream().filter(s -> station.equals(s.getName())).findFirst();
    return foundStation.map(stations::indexOf).orElse(-1);
  }

  public static Line findLineByEnum(LineEnum lineEnum, List<Line> lines) {
    Optional<Line> firstFound = lines.stream().filter(l -> lineEnum.equals(l.getName())).findFirst();
    return firstFound.orElse(null);
  }
}
