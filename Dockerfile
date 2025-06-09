# Imagen base con Amazon Corretto 24
FROM amazoncorretto:24.0.1
LABEL authors="Bell"

# Puerto que expondrá la aplicación
EXPOSE 8080

# Argumento para el archivo JAR
ARG JAR_FILE=target/DBPBackend-0.0.1.jar

# Copia del archivo JAR al contenedor
COPY ${JAR_FILE} app_cometec.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app_cometec.jar"]
