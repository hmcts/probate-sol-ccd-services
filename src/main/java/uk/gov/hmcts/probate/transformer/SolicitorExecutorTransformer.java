package uk.gov.hmcts.probate.transformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutor;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorNotApplying;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.response.ResponseCaseData;
import uk.gov.hmcts.probate.service.SolicitorExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static uk.gov.hmcts.probate.model.Constants.NO;
import static uk.gov.hmcts.probate.model.Constants.YES;
import static uk.gov.hmcts.probate.transformer.CallbackResponseTransformer.ANSWER_NO;
import static uk.gov.hmcts.probate.transformer.CallbackResponseTransformer.ANSWER_YES;

@Service
@Slf4j
@AllArgsConstructor
public class SolicitorExecutorTransformer {

    private static final String SOL_AS_EXEC_ID = "solicitor";

    public void solicitorIsApplyingTransformation(CaseData caseData, ResponseCaseData.ResponseCaseDataBuilder<?, ?> builder) {
        if (isSolicitorExecutor(caseData)) {
            if (isSolicitorApplying(caseData)) {

                // Solicitor is primary applicant
                addSolicitorAsPrimaryApplicant(caseData, builder);

            } else {

                if (getSolsSOTName(caseData.getSolsSOTForenames(), caseData.getSolsSOTSurname()).equals(caseData.getPrimaryApplicantFullName())) {
                    removeSolicitorAsPrimaryApplicant(builder);
                }

                if (caseData.getSolsSolicitorIsApplying() == null) {
                    builder
                            .solsPrimaryExecutorNotApplyingReason(null);
                }
            }
        } else {
            builder
                    .solsSolicitorIsApplying(NO)
                    .solsSolicitorNotApplyingReason(null);
        }

    }

    private void addSolicitorAsPrimaryApplicant(CaseData caseData, ResponseCaseData.ResponseCaseDataBuilder<?, ?> builder) {
        builder
                .primaryApplicantForenames(caseData.getSolsSOTForenames())
                .primaryApplicantSurname(caseData.getSolsSOTSurname())
                .primaryApplicantPhoneNumber(caseData.getSolsSolicitorPhoneNumber())
                .primaryApplicantEmailAddress(caseData.getSolsSolicitorEmail())
                .primaryApplicantAddress(caseData.getSolsSolicitorAddress())
                .primaryApplicantAlias(null)
                .primaryApplicantHasAlias(NO)
                .primaryApplicantIsApplying(YES)
                .solsSolicitorNotApplyingReason(null)
                .solsPrimaryExecutorNotApplyingReason(null);
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

    public void populateAdditionalExecutorList(CaseData caseData,
                                               SolicitorExecutorService solicitorExecutorService,
                                               ResponseCaseData.ResponseCaseDataBuilder builder) {

        // Initialise lists
        List<CollectionMember<AdditionalExecutorApplying>> execsApplying = caseData.getAdditionalExecutorsApplying() == null ?
                new ArrayList<>() : mapApplyingAdditionalExecutors(caseData);
        List<CollectionMember<AdditionalExecutorNotApplying>> execsNotApplying = caseData.getAdditionalExecutorsNotApplying() == null ?
                new ArrayList<>() : caseData.getAdditionalExecutorsNotApplying();

        // Transform lists
        if (isSolicitorExecutor(caseData) && NO.equals(caseData.getSolsSolicitorIsApplying())) {

            // Add solicitor to not applying list
            execsNotApplying = solicitorExecutorService.addSolicitorToNotApplyingList(caseData, execsNotApplying);
            execsApplying = solicitorExecutorService.removeSolicitorFromApplyingList(execsApplying);

        } else if (NO.equals(caseData.getSolsSolicitorIsExec()) || isSolicitorApplying(caseData)) {

            // Remove solicitor from executor lists as they are primary applicant
            execsApplying = solicitorExecutorService.removeSolicitorFromApplyingList(execsApplying);
            execsNotApplying = solicitorExecutorService.removeSolicitorFromNotApplyingList(execsNotApplying);

        }

        builder
                .additionalExecutorsApplying(execsApplying)
                .additionalExecutorsNotApplying(execsNotApplying);
    }

    public void otherExecutorExistsTransformation(CaseData caseData, ResponseCaseData.ResponseCaseDataBuilder builder ) {
        if (isSolicitorExecutor(caseData) && !isSolicitorApplying(caseData)) {
            builder.otherExecutorExists(YES);
        }
    }


    public void solicitorExecutorTransformation(CaseData caseData, SolicitorExecutorService solicitorExecutorService,
                     ResponseCaseData.ResponseCaseDataBuilder builder) {

        if (CollectionUtils.isEmpty(caseData.getSolsAdditionalExecutorList())) {

            if (isSolicitorExecutor(caseData)) {
                populateAdditionalExecutorList(caseData, solicitorExecutorService, builder);

            } else {
                builder
                        .additionalExecutorsApplying(caseData.getAdditionalExecutorsApplying())
                        .additionalExecutorsNotApplying(caseData.getAdditionalExecutorsNotApplying());
            }

        } else {

            // Initialise lists
            List<CollectionMember<AdditionalExecutorApplying>> applyingExec = new ArrayList<>();
            List<CollectionMember<AdditionalExecutorNotApplying>> notApplyingExec = new ArrayList<>();

            // Populate lists
            for (CollectionMember<AdditionalExecutor> additionalExec : caseData.getSolsAdditionalExecutorList()) {
                if (ANSWER_YES.equalsIgnoreCase(additionalExec.getValue().getAdditionalApplying())) {
                    applyingExec.add( new CollectionMember<>(additionalExec.getId(), buildApplyingAdditionalExecutor(additionalExec.getValue())));
                } else if (ANSWER_NO.equalsIgnoreCase(additionalExec.getValue().getAdditionalApplying())) {
                    notApplyingExec.add( new CollectionMember<>(additionalExec.getId(), buildNotApplyingAdditionalExecutor(additionalExec.getValue())));
                }
            }

            builder
                    .additionalExecutorsApplying(applyingExec)
                    .additionalExecutorsNotApplying(notApplyingExec)
                    .solsAdditionalExecutorList(EMPTY_LIST);
        }
    }

    public List<CollectionMember<AdditionalExecutor>> mapSolsAdditionalExecutors(CaseData caseData, List<CollectionMember<AdditionalExecutor>> execs,
                                                                                  SolicitorExecutorService solicitorExecutorService) {
        // Initialise list
        List<CollectionMember<AdditionalExecutor>> updatedExecs = new ArrayList<>();
        if (execs != null && !execs.isEmpty()) updatedExecs.addAll(execs);

        if (updatedExecs.stream().anyMatch(exec -> SOL_AS_EXEC_ID.equalsIgnoreCase(exec.getId()))) return updatedExecs;

        if (isSolicitorExecutor(caseData) && !isSolicitorApplying(caseData)) {

            // Add solicitor to not applying list
            updatedExecs = solicitorExecutorService.addSolicitorNotApplyingExecutor(caseData, updatedExecs);
        }

        return updatedExecs;
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

    private AdditionalExecutorApplying buildApplyingAdditionalExecutor(AdditionalExecutor additionalExecutorApplying) {
        return AdditionalExecutorApplying.builder()
                .applyingExecutorName(additionalExecutorApplying.getAdditionalExecForenames()
                        + " " + additionalExecutorApplying.getAdditionalExecLastname())
                .applyingExecutorPhoneNumber(null)
                .applyingExecutorEmail(null)
                .applyingExecutorAddress(additionalExecutorApplying.getAdditionalExecAddress())
                .applyingExecutorOtherNames(additionalExecutorApplying.getAdditionalExecAliasNameOnWill())
                .build();
    }

    private AdditionalExecutorNotApplying buildNotApplyingAdditionalExecutor(AdditionalExecutor additionalExecutorNotApplying) {
        return AdditionalExecutorNotApplying.builder()
                .notApplyingExecutorName(additionalExecutorNotApplying.getAdditionalExecForenames()
                        + " " + additionalExecutorNotApplying.getAdditionalExecLastname())
                .notApplyingExecutorReason(additionalExecutorNotApplying.getAdditionalExecReasonNotApplying())
                .notApplyingExecutorNameOnWill(additionalExecutorNotApplying.getAdditionalExecAliasNameOnWill())
                .build();
    }

    private boolean isSolicitorExecutor(CaseData caseData) { return YES.equals(caseData.getSolsSolicitorIsExec()); }

    private boolean isSolicitorApplying(CaseData caseData) { return YES.equals(caseData.getSolsSolicitorIsApplying()); }

    private String getSolsSOTName(String firstNames, String surname) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstNames);
        sb.append(" " + surname);
        return sb.toString();
    }
}
