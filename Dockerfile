FROM quay.io/keycloak/keycloak:23.0.5

COPY ./target/master_keycloak-1.0-SNAPSHOT.jar /opt/keycloak/providers
COPY ./src/main/resources/themes/ /opt/keycloak/themes

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev" , "--spi-locale-selector-provider=CustomLocale"]
