package doo.daba.java.metro.ws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class SubwayResponseError {
  @Getter @Setter
  private String stage;
  @Getter @Setter
  private List<String> errors;
}
