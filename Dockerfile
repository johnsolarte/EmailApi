# Etapa 1: Compilación
FROM openjdk:21-jdk AS builder
WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar archivos y compilar
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=builder /app/target/emailservice-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]