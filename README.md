# metro-cdmx-challenge
Implementación en Java del reto "Mexico City Metro Challenge"


Sobre el proyecto `metro-lib` ejecutar: ```mvn install```

Sobre el proyecto `metro-ws` ejecutar: ```mvn clean spring-boot:run```


El endpoint está esxpuesto en la URI `"/metro/api/v1/routes/{from}/{to}"`. Para obtener el calculo de una ruta se 
debe invocar el endpoint por el verbo GET, es importante considerar que los espacios se escapan en formato de URI 
`%20`, por ejemplo:
```http://localhost:8080/metro/api/v1/routes/Centro%20Médico/Tacuba```
