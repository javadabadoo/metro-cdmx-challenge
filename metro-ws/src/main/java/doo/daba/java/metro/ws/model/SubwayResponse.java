package doo.daba.java.metro.ws.model;

import doo.daba.java.metro.model.RouteStep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class SubwayResponse {

  @Getter @Setter
  private final List<RouteStep> routes;
  @Getter @Setter
  private List<Step> steps;
}
