package doo.daba.java.metro.parser;

import doo.daba.java.metro.exception.MetroReaderException;
import doo.daba.java.metro.factory.LineFactory;
import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.model.Station;
import lombok.Getter;
import lombok.NonNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MetroParser {

  public static final String SUBWAY_LINES_XPATH = "//text()[contains(.,'Líneas de Metro')]";
  public static final String SUBWAY_STATIONS_XPATH = "//text()[contains(.,'Estaciones de Metro')]";

  @Getter
  private Document kmlDocument;

  public MetroParser(@NonNull final String filepath) {
    this.kmlDocument = this.readKml(filepath);
  }

  private Document readKml(String filepath) {
    try (InputStream stream = MetroParser.class.getClassLoader().getResourceAsStream(filepath)) {
      kmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new MetroReaderException(
          "Error al intenar cargar el archivo " + filepath + " desde el classpath. " + e.getMessage(), e);
    }
    return kmlDocument;
  }

  public Node extractFolder(@NonNull String xpath) {
    NodeList folderNode;
    try {
      XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xpath);
      folderNode = (NodeList) expr.evaluate(this.kmlDocument, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      throw new MetroReaderException("Carambas, no existe el nodo Folder. ¿Donde chihuahuas estará?", e);
    }
    return folderNode.item(0).getParentNode().getParentNode();
  }


  public List<Station> extractUndergroundStations(@NonNull final Node folder) {
    List<Station> stations = new ArrayList<>();
    nodeListIterator(folder.getChildNodes()).forEach(n -> {
      if (n.getChildNodes().getLength() == 9) {
        String[] cord = n.getChildNodes().item(7).getTextContent().trim().split(",");
        stations.add(new Station(
            n.getChildNodes().item(1).getTextContent().trim(),
            n.getChildNodes().item(3).getTextContent().trim(),
            Double.parseDouble(cord[0]),
            Double.parseDouble(cord[1])
        ));
      }
    });
    return stations;
  }


  public List<Line> extractUndergroundLines(@NonNull Node folder) {
    List<Line> lines = new ArrayList<>();
    nodeListIterator(folder.getChildNodes()).forEach(n -> {
      if (n.getChildNodes().getLength() > 6) {
        lines.add(LineFactory.getLine(n.getChildNodes().item(1).getTextContent().trim(),
            n.getChildNodes().item(5).getChildNodes().item(3).getTextContent().trim()));
      }
    });
    return lines;
  }

  public List<Line> extractAll() {
    List<Station> stations = this.extractUndergroundStations(this.extractFolder(SUBWAY_STATIONS_XPATH));
    List<Line> lines = this.extractUndergroundLines(this.extractFolder(SUBWAY_LINES_XPATH));
    for (Line line : lines) {
      line.getStations().forEach(stationFromLine ->
          stations.stream().filter(s -> s.equals(stationFromLine)).forEach(s -> {
            s.getLines().add(line.getName());
            stationFromLine.setLines(s.getLines());
            stationFromLine.setDescription(s.getDescription());
            stationFromLine.setName(s.getName());
            stationFromLine.setLatitude(s.getLatitude());
            stationFromLine.setLongitude(s.getLongitude());
          }));
    }
    return lines;
  }

  private Iterable<Node> nodeListIterator(@NonNull final NodeList nodeList) {
    return () -> new Iterator<Node>() {
      private int index = 0;
      @Override public boolean hasNext() { return index < nodeList.getLength(); }
      @Override public Node next() { return nodeList.item(index++); }
    };
  }

}
