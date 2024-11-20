package minhtan.authenticator;

import org.keycloak.locale.DefaultLocaleSelectorProviderFactory;
import org.keycloak.locale.LocaleSelectorProvider;
import org.keycloak.models.KeycloakSession;

public class CustomLocaleSelectorProviderFactory extends DefaultLocaleSelectorProviderFactory {

    @Override
    public LocaleSelectorProvider create(KeycloakSession session) {
       return new CustomLocaleSelectorProvider(session);
    }

    @Override
    public String getId() {
        return "CustomLocale";
    }
}
