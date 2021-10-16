package doo.daba.java.metro.factory;

import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.LineEnum;
import doo.daba.java.metro.model.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineFactory {

  private LineFactory() {}

  public static Line getLine(String name, String stationCords) {
    List<Station> stations = new ArrayList<>();
    Arrays.stream(stationCords.replaceAll("[ \n]", "").split(",0")).forEach(cords -> {
      String[] cord = cords.split(",");
      stations.add(new Station(Double.parseDouble(cord[0]), Double.parseDouble(cord[1])));
    });
    return new Line(LineEnum.fromName(name), stations);
  }
}
