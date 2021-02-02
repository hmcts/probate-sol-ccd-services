package uk.gov.hmcts.probate.model.ccd.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalExecutorTrustCorps {

    private final String additionalExecForenames;
    private final String additionalExecLastname;
    private final String additionalExecNameOnWill;
    private final String additionalExecAliasNameOnWill;
    private final String additionalExecutorTrustCorpPosition;
    private final SolsAddress additionalExecAddress;

}
