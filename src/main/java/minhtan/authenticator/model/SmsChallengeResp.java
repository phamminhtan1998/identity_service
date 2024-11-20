package minhtan.authenticator.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SmsChallengeResp implements Serializable {
    private String sessionId;
    private String code;
    private String loginActionUrl;
    private String actionUrl;
    private String qrImg;
}