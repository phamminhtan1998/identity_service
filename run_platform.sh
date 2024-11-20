mvn clean package

docker rmi -f master-keycloak
docker build -t master-keycloak .
docker rm -f mykeycloak
docker run --name mykeycloak  -e JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787" -p 8443:8080 -p 8787:8787 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin --env KC_LOG_LEVEL=DEBUG --network=host -v keycloak_data:/opt/keycloak/data master-keycloak:latest

