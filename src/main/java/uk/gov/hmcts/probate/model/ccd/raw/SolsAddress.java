package uk.gov.hmcts.probate.model.ccd.raw;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.probate.controller.validation.ApplicationUpdatedGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@Data
@Builder
public class SolsAddress implements Serializable {

    @NotNull(groups = {ApplicationUpdatedGroup.class}, message = "{deceasedAddressIsNull}")
    @Size(min = 1, groups = {ApplicationUpdatedGroup.class}, message = "{deceasedAddressIsNull}")
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

    @NotNull(groups = {ApplicationUpdatedGroup.class}, message = "{deceasedPostcodeIsNull}")
    @Size(min = 1, groups = {ApplicationUpdatedGroup.class}, message = "{deceasedPostcodeIsNull}")
    @JsonProperty(value = "PostCode")
    private final String postCode;

    @JsonProperty(value = "Country")
    private final String country;

    @Override
    public SolsAddress clone() {
        // super.clone() is throwing CloneNotSupportedException
        return SolsAddress.builder()
                .addressLine1(addressLine1)
                .addressLine2(addressLine2)
                .addressLine3(addressLine3)
                .county(county)
                .country(country)
                .postCode(postCode)
                .postTown(postTown)
                .build();
    }
}
