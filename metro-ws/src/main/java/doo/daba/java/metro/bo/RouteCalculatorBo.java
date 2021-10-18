package doo.daba.java.metro.bo;

import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.RouteStep;
import doo.daba.java.metro.util.RouteFinder;
import doo.daba.java.metro.ws.model.SubwayResponse;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorBo {

  public SubwayResponse calculateRoute(@NonNull final String from, @NonNull final String to,
                                       @NonNull final List<Line> lines) {
    List<RouteStep> routes = RouteFinder.findRoute(from, to, lines);
    /*
     TODO: Agregar el resumen de los cambios de estación como explicación corta. Considerando una Ruta de
      Centro Médico a Tacuba, los pasos de una ruta podrian ser:
      - Centro Médico Linea 3
      - Hidalgo Linea 2
      - Tacuba Linea 2
    */
    return new SubwayResponse(routes, new ArrayList<>());
  }
}
