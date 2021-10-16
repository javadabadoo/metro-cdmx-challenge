package doo.daba.java.metro;

import doo.daba.java.metro.exception.MetroReaderException;
import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.Station;
import doo.daba.java.metro.parser.MetroParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
  private final MetroParser parser = new MetroParser("Metro_CDMX.kml");

  @Test
  @DisplayName("KML file not found")
  public void testExistsKmlFIle() {
    assertThrowsExactly(IllegalArgumentException.class, () -> new MetroParser("unknownFile.kml"));

  }

  @Test
  @DisplayName("Load KML file")
  public void testReadFromFile() {
    assertNotNull(this.parser.getKmlDocument());
  }

  @Test
  @DisplayName("KML node Element")
  public void testRootElement() {
    assertEquals("kml", this.parser.getKmlDocument().getDocumentElement().getNodeName());
  }

  @Test
  @DisplayName("Testing nullability")
  public void testNullXpath() {
    assertThrowsExactly(NullPointerException.class, () -> this.parser.extractFolder(null));
  }

  @Test
  @DisplayName("Exist Subway Lines node")
  public void testExistsLinesNode() {
    Node folder = this.parser.extractFolder(MetroParser.SUBWAY_LINES_XPATH);
    assertNotNull(folder);
  }

  @Test
  @DisplayName("Exist Subway Stations node")
  public void testExistsStationsNode() {
    Node folder = this.parser.extractFolder(MetroParser.SUBWAY_STATIONS_XPATH);
    assertNotNull(folder);
  }

  @Test
  @DisplayName("Bad xpath")
  public void testBadNode() {
    assertThrowsExactly(MetroReaderException.class, () -> this.parser.extractFolder("XPATH ERROR"));
  }

  @Test
  @DisplayName("Extract all subway stations")
  public void testExtractStations() {
    Node folder = this.parser.extractFolder(MetroParser.SUBWAY_STATIONS_XPATH);
    List<Station> stations = this.parser.extractUndergroundStations(folder);
    assertNotNull(stations);
    assert !stations.isEmpty();
  }

  @Test
  @DisplayName("Extract all subway lines")
  public void testExtractLines() {
    Node folder = this.parser.extractFolder(MetroParser.SUBWAY_LINES_XPATH);
    List<Line> lines = this.parser.extractUndergroundLines(folder);
    assertNotNull(lines);
    assert !lines.isEmpty();
  }

  @Test
  @DisplayName("Extract all data (Lines with all its stations info)")
  public void testExtractAll() {
    List<Line> lines = this.parser.extractAll();
    assertNotNull(lines);
    assert !lines.isEmpty();
  }

}
