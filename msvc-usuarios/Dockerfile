FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app/msvc-usuarios

COPY ./pom.xml /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-usuarios/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .

ENV PORT_USUARIOS 8000

EXPOSE $PORT_USUARIOS

#ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]

CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]

# docker network create spring
# docker build -t usuarios:latest . -f .\msvc-usuarios\Dockerfile
# docker run -d -p 8001:8001 --rm --name msvc-usuarios --network spring usuarios
# docker container inspect msvc-usuarios
# docker pull mysql:8 imagen de mysql
# docker run -d -p 3307:3306 --name mysql8 --network spring -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=msvc_usuarios mysql:8
# agregar un volumen -v data-mysql:/var/lib/mysql
# reiniciar container --restart=always
# docker run -d -p 3307:3306 --name mysql8 --network spring -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=msvc_usuarios -v data-mysql:/var/lib/mysql --restart=always mysql:8
# crear la base de datos en msql workbench con el puerto 3307
# docker start mysql8
# run container con --env-file
# docker run -d -p 8001:8091 --env-file .\.env --rm --name msvc-usurios --network spring usuarios
# docker run -d -p 8001:8091 --env-file .\.env --rm --name msvc_usuarios_container --network spring usuarios_image