package minhtan.authenticator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SmsData {

    private String sessionId;
    private String userId;
    private Long timeInit;
    private boolean status;
}
