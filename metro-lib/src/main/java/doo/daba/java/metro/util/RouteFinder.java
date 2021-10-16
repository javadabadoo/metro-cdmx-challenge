package doo.daba.java.metro.util;

import doo.daba.java.metro.exception.RouteException;
import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.LineEnum;
import doo.daba.java.metro.model.Station;
import lombok.NonNull;

import java.util.*;

public class RouteFinder {
  private RouteFinder() {}

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

  public static List<Line> findRoute(@NonNull final String from, @NonNull final String to,
                                     @NonNull final List<Line> lines) {
    List<LineEnum> lineFrom = findLineByStationName(from, lines);
    List<LineEnum> lineTo = findLineByStationName(to, lines);
    if (lineFrom == null || lineTo == null) {
      throw new RouteException("Wow, it seems you're looking for an invalid route.");
    }
    /*
      TODO:
       Necesitamos recortar las lista de las lineas para que se retorne solo el segmento a recorrer
       Considerar la direción del viaje
       Buscar Lineas secundarias a partir de las estaciones de enlace
     */
    return null;
  }
}
