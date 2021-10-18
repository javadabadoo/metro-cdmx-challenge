package doo.daba.java.metro.model;

import lombok.Getter;

/**
 * Valores permitidos para indicar los numeros de las lineas
 */
public enum LineEnum {
  LINEA_1("Línea 1"),
  LINEA_2("Línea 2"),
  LINEA_3("Línea 3"),
  LINEA_4("Línea 4"),
  LINEA_5("Línea 5"),
  LINEA_6("Línea 6"),
  LINEA_7("Línea 7"),
  LINEA_8("Línea 8"),
  LINEA_9("Línea 9"),
  LINEA_10("Línea 10"),
  LINEA_11("Línea 11"),
  LINEA_12("Línea 12"),
  LINEA_A("Línea A"),
  LINEA_B("Línea B");

  @Getter
  private String name;

  private LineEnum (String name){
    this.name = name;
  }

  /**
   * Mediante el valor del texto del nombre de la linea se busca el {@code Enum} correspondiente
   *
   * @param name
   * @return Enum que representa a la linea de acuerdo al texto recibido
   */
  public static LineEnum fromName(String name) {
    for (LineEnum e : LineEnum.values()) {
      if (e.getName().equals(name)) return e;
    }
    return null;
  }
}
