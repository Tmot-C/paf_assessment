# Use the Maven image as the base
# add compiler here
FROM maven:3.9.9-eclipse-temurin-23 AS compiler


# Define application directory
ARG APP_DIR=/app
WORKDIR ${APP_DIR}

# Copy project files into the image
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY src src

# Ensure the mvnw script has execution permissions
RUN chmod +x mvnw

# Build the application
#./mvnw
RUN mvn package -Dmaven.test.skip=true

# # Set the server port
# ENV SERVER_PORT=4000

# # Expose the port
# EXPOSE ${SERVER_PORT}

# # Run the application
# ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar target/ssfproject-SNAPSHOT.jar

#docker build -t itsjonlol/vttp2023_batch4_paf:0.0.1 . 

#Stage 2
FROM maven:3.9.9-eclipse-temurin-23

ARG DEPLOY_DIR=/code_folder

WORKDIR ${DEPLOY_DIR}
#change the names here
COPY --from=compiler /app/target/movies-0.0.1-SNAPSHOT.jar paf_bf_assessment.jar

# Set the server port
ENV SERVER_PORT=8080

# Expose the port
EXPOSE ${SERVER_PORT}
# ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/party
# ENV SPRING_DATASOURCE_USERNAME=fred
# ENV SPRING_DATASOURCE_PASSWORD=fred
#change the name here
# Run the application
ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar paf_bf_assessment.jar