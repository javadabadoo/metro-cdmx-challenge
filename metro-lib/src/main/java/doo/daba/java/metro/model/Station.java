package doo.daba.java.metro.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Station {
  @Getter @Setter
  private List<LineEnum> lines;
  @Getter @Setter
  private String name;
  @Getter @Setter
  private String description;
  @Getter @Setter
  private double latitude;
  @Getter @Setter
  private double longitude;

  public Station(String name, String description, double latitude, double longitude) {
    this.lines = new ArrayList<>();
    this.name = name;
    this.description = description;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Station(double latitude, double longitude) {
    this.lines = new ArrayList<>();
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Station station = (Station) o;
    return Double.compare(station.latitude, latitude) == 0 && Double.compare(station.longitude, longitude) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(latitude);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(longitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
