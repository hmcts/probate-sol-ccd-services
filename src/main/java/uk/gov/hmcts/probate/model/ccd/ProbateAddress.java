package uk.gov.hmcts.probate.model.ccd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProbateAddress implements Serializable {

    @JsonProperty(value = "AddressLine1")
    private final String addressLine1;

    @JsonProperty(value = "AddressLine2")
    private final String addressLine2;

    @JsonProperty(value = "AddressLine3")
    private final String addressLine3;

    @JsonProperty(value = "County")
    private final String county;

    @JsonProperty(value = "PostTown")
    private final String postTown;

    @JsonProperty(value = "Country")
    private final String country;

    @JsonProperty(value = "PostCode")
    private final String postCode;
}
