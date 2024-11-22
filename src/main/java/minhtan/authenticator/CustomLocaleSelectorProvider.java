package minhtan.authenticator;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.locale.DefaultLocaleSelectorProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Locale;

@JBossLog
public class CustomLocaleSelectorProvider extends DefaultLocaleSelectorProvider {
    public CustomLocaleSelectorProvider(KeycloakSession session) {
        super(session);
    }

    @Override
    public Locale resolveLocale(RealmModel realm, UserModel user) {
        String defaultLocale = realm.getDefaultLocale();
        log.infof("Just a sample 3");
        log.infof("set default locale : %s", defaultLocale);
        return  new Locale(defaultLocale);
    }
}
