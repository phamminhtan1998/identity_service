package minhtan.authenticator.event;

import com.google.auto.service.AutoService;
import minhtan.authenticator.kafka.Producer;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

@AutoService(EventListenerProviderFactory.class)
public class CustomLoginEventListenerFactory implements EventListenerProviderFactory {

    public static final String CUSTOM_LOGIN_EVENT_LISTENER_ID = "custom event listener";

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new CustomLoginEventListener(keycloakSession, new Producer());
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return CUSTOM_LOGIN_EVENT_LISTENER_ID;
    }
}
