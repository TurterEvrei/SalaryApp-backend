FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /build

ADD target/SalaryApp-0.0.1-SNAPSHOT-exec.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /application

COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher ${@}