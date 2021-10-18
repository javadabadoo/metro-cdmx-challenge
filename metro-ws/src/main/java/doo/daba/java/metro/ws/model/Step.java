package doo.daba.java.metro.ws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Step {
  @Getter@Setter
  private String line;
  @Getter@Setter
  private String station;

}
