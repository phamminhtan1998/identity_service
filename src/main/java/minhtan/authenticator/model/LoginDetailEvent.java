package minhtan.authenticator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDetailEvent {
    private String userId;
    private String eventType;
    private Long currentTime;
    private String status;
    private String realmId;
    private String ipAddress;
    private String error;
}
