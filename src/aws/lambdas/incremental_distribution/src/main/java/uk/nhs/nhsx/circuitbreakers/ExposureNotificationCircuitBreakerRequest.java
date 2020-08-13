package uk.nhs.nhsx.circuitbreakers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExposureNotificationCircuitBreakerRequest {

    public final int matchedKeyCount;
    public final int daysSinceLastExposure;
    public final int maximumRiskScore;

    @JsonCreator
    public ExposureNotificationCircuitBreakerRequest(
        @JsonProperty int matchedKeyCount,
        @JsonProperty int daysSinceLastExposure,
        @JsonProperty int maximumRiskScore
    ) {

        this.matchedKeyCount = matchedKeyCount;
        this.daysSinceLastExposure = daysSinceLastExposure;
        this.maximumRiskScore = maximumRiskScore;
    }


}
