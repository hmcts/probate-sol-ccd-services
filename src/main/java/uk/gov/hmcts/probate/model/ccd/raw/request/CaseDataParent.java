package uk.gov.hmcts.probate.model.ccd.raw.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorActingForTrustCorp;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;

import java.time.LocalDate;
import java.util.List;

@Jacksonized
@SuperBuilder
@Data
public class CaseDataParent {

    protected String registrySequenceNumber;
    protected final String deceasedDeathCertificate;
    protected final String deceasedDiedEngOrWales;
    protected final String deceasedForeignDeathCertInEnglish;
    protected final String deceasedForeignDeathCertTranslation;

    protected String dispenseWithNotice;
    protected String titleAndClearingType;
    protected String trustCorpName;
    protected String actingTrustCorpName;
    protected String positionInTrustCorp;
    protected String othersActingForTrustCorp;
    protected List<CollectionMember<AdditionalExecutorActingForTrustCorp>> othersActingForTrustCorpList;
    protected String lodgementAddress;
    protected LocalDate lodgementDate;
}
