FROM openjdk:11-jdk-slim AS build

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
RUN mvn -B -f pom.xml dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests


FROM tomcat:9.0-jdk11

COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]

# docker build -t asbank2023 .
# docker run --name asbank2023 -p 8082:8080 asbank2023