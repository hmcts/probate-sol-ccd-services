package uk.gov.hmcts.probate.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.exception.BusinessValidationException;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseDetails;
import uk.gov.hmcts.probate.service.BusinessValidationMessageRetriever;

import java.util.Locale;

import static uk.gov.hmcts.probate.model.Constants.NO;
import static uk.gov.hmcts.probate.model.Constants.NON_TRUST_PTNR_TITLE_CLEARING_TYPES_NOT_ALL_RENOUNCING;
import static uk.gov.hmcts.probate.model.Constants.TRUST_CORP_TITLE_CLEARING_TYPES;
import static uk.gov.hmcts.probate.model.Constants.YES;

@Component
@RequiredArgsConstructor
public class PartnersAddedValidationRule implements TitleAndClearingPageValidationRule {

    private final BusinessValidationMessageRetriever businessValidationMessageRetriever;
    private static final String PARTNERS_NEEDED = "partnersNeeded";
    private static final String PARTNERS_NEEDED_TRUST_CORPS = "partnersNeededTrustCorp";

    @Override
    public void validate(CaseDetails caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final String[] args = {caseDetails.getId().toString()};
        final String titleAndClearing = caseData.getTitleAndClearingType();
        final String userMessage = businessValidationMessageRetriever.getMessage(PARTNERS_NEEDED, args, Locale.UK);
        final String userMessageTrustCorps = businessValidationMessageRetriever.getMessage(PARTNERS_NEEDED_TRUST_CORPS,
                args, Locale.UK);

        // Must have other partners for trust corp / firm if NOT (not named in will, and applying)

        if (!(NO.equals(caseData.getSolsSolicitorIsExec()) && YES.equals(caseData.getSolsSolicitorIsApplying()))) {
            if (NO.equals(caseData.getAnyOtherApplyingPartners())
                    && NON_TRUST_PTNR_TITLE_CLEARING_TYPES_NOT_ALL_RENOUNCING.contains(titleAndClearing)) {
                throw new BusinessValidationException(userMessage,
                        "'Yes' needs to be selected for question anyOtherApplyingPartners for case id "
                                + caseDetails.getId());
            }

            if (NO.equals(caseData.getAnyOtherApplyingPartnersTrustCorp())
                    && TRUST_CORP_TITLE_CLEARING_TYPES.contains(titleAndClearing)) {
                throw new BusinessValidationException(userMessageTrustCorps,
                        "'Yes' needs to be selected for question anyOtherApplyingPartnersTrustCorp for case id "
                                + caseDetails.getId());
            }
        }
    }
}
