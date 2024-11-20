package minhtan.authenticator;

import com.google.auto.service.AutoService;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import minhtan.authenticator.model.ChallengeReturnBody;
import minhtan.authenticator.model.DeviceRequestModel;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.utils.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@AutoService(value = AuthenticatorFactory.class)
@JBossLog
public class DeviceRegistrationAuthenticator extends AbstractDirectGrantAuthenticator {

    public static final String DEVICE_REGISTRATION_ID = "device-registration-id";
    public static final String DEVICE_ID = "device-id";
    public static final String USER_ID = "user-id";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        log.debug("Registration Flow");
        DeviceRequestModel deviceRequest = getDeviceRequest(context);
        if (StringUtil.isNotBlank(deviceRequest.getDeviceId()) && StringUtil.isNotBlank(deviceRequest.getUser())) {
            log.infof("Registration flow success");
            context.success();
            return;
        } else {
            log.infof("Registration Flow Init");
            initDeviceRegChallenge(context);
        }
    }

    public DeviceRequestModel getDeviceRequest(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> request = context.getHttpRequest().getDecodedFormParameters();
        DeviceRequestModel deviceRequestModel = DeviceRequestModel.builder()
                .deviceId(request.getFirst(DEVICE_ID))
                .user(request.getFirst(USER_ID))
                .build();
        return deviceRequestModel;
    }

    public void initDeviceRegChallenge(AuthenticationFlowContext context) {
        context.challenge(Response.status(Response.Status.UNAUTHORIZED).header("WWW-Authenticate", "Bearer challenge_types=device-key").header("Content-Type", "application/json").entity(this.createKeyChallengeResponse()).build());
    }

    private ChallengeReturnBody createKeyChallengeResponse() {
        ChallengeReturnBody responseBody = ChallengeReturnBody.builder()
                .challengeType("device-registration")
                .build();
        return responseBody;
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public String getDisplayType() {
        return "Device Registration";
    }

    @Override
    public String getReferenceCategory() {
        return "minhtan";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.DISABLED,
                AuthenticationExecutionModel.Requirement.CONDITIONAL
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Registration Device with System";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return
                ProviderConfigurationBuilder.create().property()
                        .name("device-reg-request-expiry-key")
                        .label("Device registration request expiry period in seconds")
                        .type("String")
                        .defaultValue(300)
                        .helpText("The amount of time in seconds that a device registration request will be valid for. Defaults to 300 seconds")
                        .add()
                        .build();
    }

    @Override
    public String getId() {
        return DEVICE_REGISTRATION_ID;
    }
}
