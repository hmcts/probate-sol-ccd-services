package uk.gov.hmcts.probate.transformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorNotApplying;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.response.ResponseCaseData;
import uk.gov.hmcts.probate.service.SolicitorExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static uk.gov.hmcts.probate.model.Constants.NO;
import static uk.gov.hmcts.probate.model.Constants.YES;

@Service
@Slf4j
@AllArgsConstructor
public class SolicitorExecutorTransformer {

    public void mainApplicantTransformation(CaseData caseData, ResponseCaseData.ResponseCaseDataBuilder<?, ?> builder) {
        if (isSolicitorExecutor(caseData)) {
            if (isSolicitorMainApplicant(caseData)) {
                builder
                        .primaryApplicantForenames(caseData.getSolsSOTForenames())
                        .primaryApplicantSurname(caseData.getSolsSOTSurname())
                        .primaryApplicantPhoneNumber(caseData.getSolsSolicitorPhoneNumber())
                        .primaryApplicantEmailAddress(caseData.getSolsSolicitorEmail())
                        .primaryApplicantAddress(caseData.getSolsSolicitorAddress())
                        .primaryApplicantAlias(null)
                        .primaryApplicantHasAlias(NO)
                        .primaryApplicantIsApplying(YES)
                        .solsSolicitorIsApplying(YES)
                        .solsSolicitorNotApplyingReason(null)
                        .solsPrimaryExecutorNotApplyingReason(null);
            } else {
                if (getSolsSOTName(caseData.getSolsSOTForenames(), caseData.getSolsSOTSurname()).equals(caseData.getPrimaryApplicantFullName())) {
                    removeSolicitorAsPrimaryApplicant(builder);
                }

                if (caseData.getSolsSolicitorIsApplying() == null || YES.equals(caseData.getSolsSolicitorIsApplying())) {
                    builder
                            .solsPrimaryExecutorNotApplyingReason(null);
                }
            }
        } else {
            builder
                    .solsSolicitorIsMainApplicant(null)
                    .solsSolicitorIsApplying(null)
                    .solsSolicitorNotApplyingReason(null);
        }

    }

    public void solicitorExecutorTransformation(CaseData caseData,
                                                SolicitorExecutorService solicitorExecutorService,
                                                ResponseCaseData.ResponseCaseDataBuilder builder) {

        // Initialise lists
        List<CollectionMember<AdditionalExecutorApplying>> execsApplying = caseData.getAdditionalExecutorsApplying() == null ?
                new ArrayList<>() : mapApplyingAdditionalExecutors(caseData);

        List<CollectionMember<AdditionalExecutorNotApplying>> execsNotApplying = caseData.getAdditionalExecutorsNotApplying() == null ?
                new ArrayList<>() : caseData.getAdditionalExecutorsNotApplying();

        // Transform lists
        if (YES.equals(caseData.getSolsSolicitorIsExec()) && !isSolicitorMainApplicant(caseData)) {
            if (YES.equals(caseData.getSolsSolicitorIsApplying())) {
                execsApplying = solicitorExecutorService.updateSolicitorApplyingExecutor(caseData, execsApplying);
                execsNotApplying = solicitorExecutorService.removeSolicitorAsNotApplyingExecutor(execsNotApplying);
            } else if (NO.equals(caseData.getSolsSolicitorIsApplying())) {
                execsNotApplying = solicitorExecutorService.updateSolicitorNotApplyingExecutor(caseData, execsNotApplying);
                execsApplying = solicitorExecutorService.removeSolicitorAsApplyingExecutor(execsApplying);
            }
        } else if (NO.equals(caseData.getSolsSolicitorIsExec()) || isSolicitorMainApplicant(caseData)) {
            execsApplying = solicitorExecutorService.removeSolicitorAsApplyingExecutor(execsApplying);
            execsNotApplying = solicitorExecutorService.removeSolicitorAsNotApplyingExecutor(execsNotApplying);
        }

        builder
                .additionalExecutorsApplying(execsApplying)
                .additionalExecutorsNotApplying(execsNotApplying);
    }

    private void removeSolicitorAsPrimaryApplicant(ResponseCaseData.ResponseCaseDataBuilder<?, ?> builder) {
        builder
                .primaryApplicantForenames(null)
                .primaryApplicantSurname(null)
                .primaryApplicantPhoneNumber(null)
                .primaryApplicantEmailAddress(null)
                .primaryApplicantAddress(null)
                .primaryApplicantAlias(null)
                .primaryApplicantHasAlias(null)
                .primaryApplicantIsApplying(null)
                .solsPrimaryExecutorNotApplyingReason(null);
    }

    private List<CollectionMember<AdditionalExecutorApplying>> mapApplyingAdditionalExecutors(CaseData caseData) {
        return caseData.getAdditionalExecutorsApplying()
                    .stream()
                    .map(this::buildApplyingAdditionalExecutors)
                    .collect(Collectors.toList());
    }

    private CollectionMember<AdditionalExecutorApplying> buildApplyingAdditionalExecutors(CollectionMember<AdditionalExecutorApplying> additionalExecutorApplying) {
        AdditionalExecutorApplying tempExec = additionalExecutorApplying.getValue();

        if (tempExec.getApplyingExecutorName() == null) {
            additionalExecutorApplying.getValue().setApplyingExecutorName(tempExec.getApplyingExecutorFirstName()
                    + " " + tempExec.getApplyingExecutorLastName());
        }

        return additionalExecutorApplying;
    }

    private boolean isSolicitorExecutor(CaseData caseData) {
        return YES.equals(caseData.getSolsSolicitorIsExec());
    }

    private boolean isSolicitorMainApplicant(CaseData caseData) { return YES.equals(caseData.getSolsSolicitorIsMainApplicant()); }

    private String getSolsSOTName(String firstNames, String surname) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstNames);
        sb.append(" " + surname);
        return sb.toString();
    }
}
