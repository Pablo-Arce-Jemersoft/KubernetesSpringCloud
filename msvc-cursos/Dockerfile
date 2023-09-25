FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app/msvc-cursos

COPY ./pom.xml /app
COPY ./msvc-cursos/.mvn ./.mvn
COPY ./msvc-cursos/mvnw .
COPY ./msvc-cursos/pom.xml .

RUN ./mvnw dependency:go-offline
COPY ./msvc-cursos/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR app/
RUN mkdir ./logs
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .
EXPOSE 8002

CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]

# docker build -t cursos:latest . -f .\msvc-cursos\Dockerfile
# docker run -d -p 8002:8002 --rm --name msvc-cursos --network spring cursos
# docker pull postgres:14.9-alpine imagen de docker para postgres
# agregar un volumen -v data-postges:/var/lib/postgesql/data
# reiniciar container --restart=always
# docker run -d -p 5532:5432 --name postgres14 --network spring -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=msvc_cursos -v data-postges:/var/lib/postgesql/data --restart=always postgres:14.9-alpine
# docker start postgres14
