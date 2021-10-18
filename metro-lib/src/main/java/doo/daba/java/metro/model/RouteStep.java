package doo.daba.java.metro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class RouteStep {
  @Getter @Setter
  private List<Line> lines;

  public Station getInitial() {
    return lines.get(0).getStations().get(0);
  }

  public Station getDestination() {
    List<Station> stations = this.lines.get(this.lines.size() - 1).getStations();
    return stations.get(stations.size() - 1);
  }
}
