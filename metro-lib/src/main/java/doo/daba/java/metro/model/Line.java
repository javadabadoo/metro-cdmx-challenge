package doo.daba.java.metro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public class Line {
  @Getter @Setter
  private LineEnum name;
  @Getter @Setter
  private List<Station> stations;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass() && o.getClass() != LineEnum.class) return false;
    return o.getClass() == LineEnum.class ? this.name == o : ((Line) o).getName().equals(name);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
