package minhtan.authenticator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeviceRequestModel {
    private String deviceId;
    private String user;
}
