package minhtan.authenticator.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import minhtan.authenticator.kafka.Producer;
import minhtan.authenticator.model.LoginDetailEvent;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.utils.StringUtil;

@JBossLog
@RequiredArgsConstructor
public class CustomLoginEventListener implements EventListenerProvider {

    public static final String EVENT_TOPIC = "keycloak-event";
    private final KeycloakSession keycloakSession;
    private final Producer producer;


    @Override
    public void onEvent(Event event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.infof("Handle custom login in keycloak :%s", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        EventType eventType = event.getType();
        if (eventType == EventType.LOGIN
                || eventType == EventType.LOGIN_ERROR
        ) {
            log.infof("Received Login type :%d ", event.getTime());
            String eventRecord = convertToEventRecord(event);
            if (eventRecord != null) {
                log.infof("Send Sms Success");
                producer.publishEvent(EVENT_TOPIC, eventRecord);
            }
        }
    }

    private String convertToEventRecord(Event event) {

        LoginDetailEvent eventDetail = LoginDetailEvent.builder()
                .userId(event.getUserId())
                .error(event.getError())
                .eventType(event.getType().toString())
                .realmId(event.getRealmId())
                .ipAddress(event.getIpAddress())
                .currentTime(event.getTime())
                .status(StringUtil.isBlank(event.getError()) ? "SUCCESS" : "FAIL")
                .build();
        ObjectMapper obj = new ObjectMapper();
        String record = null;
        try {
            record = obj.writeValueAsString(eventDetail);
        } catch (JsonProcessingException e) {
            log.infof("Error event record :{}", e.getMessage());
        }
        return record;

    }


    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {

    }

    @Override
    public void close() {

    }
}
