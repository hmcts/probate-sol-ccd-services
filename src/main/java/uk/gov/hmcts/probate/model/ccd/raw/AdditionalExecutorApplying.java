package uk.gov.hmcts.probate.model.ccd.raw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AdditionalExecutorApplying {

    private final String applyingExecutorFirstName;
    private final String applyingExecutorLastName;
    private final String applyingExecutorTrustCorpPosition;
    // Professional, TrustCorporation, or Named
    private final String applyingExecutorType;
    private final String applyingExecutorPhoneNumber;
    private final String applyingExecutorEmail;
    private SolsAddress applyingExecutorAddress;
    private String applyingExecutorName;
    private String applyingExecutorOtherNames;
    private String applyingExecutorOtherNamesReason;
    private String applyingExecutorOtherReason;

    @Override
    public AdditionalExecutorApplying clone() {
        try {
            final AdditionalExecutorApplying exec = (AdditionalExecutorApplying) super.clone();
            exec.setApplyingExecutorAddress(this.applyingExecutorAddress.clone());
            return exec;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
