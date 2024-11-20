package minhtan.authenticator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ChallengeReturnBody {
    private String challengeType;

}
