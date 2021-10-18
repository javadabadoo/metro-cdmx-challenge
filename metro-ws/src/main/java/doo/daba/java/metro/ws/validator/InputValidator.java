package doo.daba.java.metro.ws.validator;

import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.util.RouteFinder;
import doo.daba.java.metro.ws.SubwayEndpoint;

import java.util.List;

public class InputValidator {

  public boolean validateInputParam (String param, List<Line> lines) {
    return RouteFinder.findLineByStationName(param, lines) != null;
  }

}
