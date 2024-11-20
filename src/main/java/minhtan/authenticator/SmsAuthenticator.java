package minhtan.authenticator;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import minhtan.authenticator.model.QRResp;
import minhtan.authenticator.model.SmsChallengeResp;
import minhtan.authenticator.model.SmsData;
import minhtan.authenticator.model.SmsError;
import minhtan.authenticator.provider.SmsProvider;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.utils.StringUtil;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;


public class SmsAuthenticator implements Authenticator {

    private final KeycloakSession keycloakSession;
    private final OkHttpClient httpClient;

    private final SmsProvider smsProvider;


    Logger log = Logger.getLogger(SmsAuthenticator.class);

    public static final String CODE = "code";

    public SmsAuthenticator(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
        this.httpClient = new OkHttpClient();
        this.smsProvider = new SmsProvider();
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> smsReq = context.getHttpRequest().getDecodedFormParameters();
        String code = smsReq.getFirst("code");
        String username = context.getUser().getUsername();

        if (StringUtil.isNotBlank(code)) {
            handleSmsCode(username, context);
            return;
        }
        log.info("Start Auth sms ");
        SmsChallengeResp smsChallengeResp = new SmsChallengeResp();
        smsChallengeResp.setCode("123456");
        smsChallengeResp.setSessionId(context.generateAccessCode());
        log.infof("Challenge resp %s", smsChallengeResp.toString());
        String qrImg = smsProvider.generateQRCode(getCurrentUserInSession(context));
        smsChallengeResp.setQrImg(qrImg);
        context.challenge(Response.status(HttpStatus.SC_UNAUTHORIZED).entity(smsChallengeResp).build());
        return;
    }

    public boolean validateOtp(String username, int otp) {
        log.infof("Get Sms Data with userId :%s , %s", username, otp);
        return smsProvider.validOtp(username, otp);
    }

    public String getCurrentUserInSession(AuthenticationFlowContext context) {
        UserModel user = context.getUser();
        log.infof("Current User Login : %s", user.getUsername());
        return user.getUsername();
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {

    }

    public void handleSmsCode(String username, AuthenticationFlowContext context) {
        log.infof("Continue action sms login");
        MultivaluedMap<String, String> smsReq = context.getHttpRequest().getDecodedFormParameters();
        int code = Integer.parseInt(smsReq.getFirst("code"));
        boolean isValidOTP = validateOtp(username, code);
        if (isValidOTP) {
            log.infof("Otp is valid");
            context.success();
            return;
        }
        SmsError err = new SmsError();
        err.setError("Invalid Code");
        err.setErrorDescription("Invalid sms code");
        err.setErrorAt(new Date());
        context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, Response.status(HttpStatus.SC_BAD_REQUEST).entity(err).build());

    }
    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }

}
