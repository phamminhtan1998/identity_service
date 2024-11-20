package minhtan.authenticator.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class OTPReq {
    private String username;
    private int otp;
}
