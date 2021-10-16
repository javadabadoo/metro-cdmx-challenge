package doo.daba.java.metro.exception;

public class RouteException extends RuntimeException {

  public RouteException(String message, Throwable ex) {
    super(message, ex);
  }

  public RouteException(String message) {
    super(message);
  }
}
