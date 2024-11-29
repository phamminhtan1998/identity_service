#mvn clean package

docker rmi -f master-keycloak
docker build -t master-keycloak .
