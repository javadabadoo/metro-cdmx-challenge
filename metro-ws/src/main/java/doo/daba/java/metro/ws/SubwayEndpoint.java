package doo.daba.java.metro.ws;

import doo.daba.java.metro.bo.RouteCalculatorBo;
import doo.daba.java.metro.model.Line;
import doo.daba.java.metro.parser.MetroParser;
import doo.daba.java.metro.ws.model.SubwayResponse;
import doo.daba.java.metro.ws.validator.InputValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SubwayEndpoint {

  final List<Line> lines = new MetroParser("Metro_CDMX.kml").extractAll();

  @GetMapping("/metro/api/v1/routes/{from}/{to}")
  public SubwayResponse greeting(
      @PathVariable(required = true, name = "from") String from,
      @PathVariable(required = true, name = "to") String to) {
    InputValidator validator = new InputValidator();
    if (!validator.validateInputParam(from, lines)) {
    }
    if (!validator.validateInputParam(to, lines)) {
    }
    RouteCalculatorBo calculator = new RouteCalculatorBo();

    return calculator.calculateRoute(from, to, lines);
  }
}
