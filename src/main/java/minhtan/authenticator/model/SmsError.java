package minhtan.authenticator.model;

import lombok.Data;

import java.util.Date;

@Data
public class SmsError {
    private String error ;
    private String errorDescription;
    private Date errorAt;
}
