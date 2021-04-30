package uk.gov.hmcts.probate.transformer.solicitorexecutors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorApplying;
import uk.gov.hmcts.probate.model.ccd.raw.AdditionalExecutorNotApplying;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.service.DateFormatterService;
import uk.gov.hmcts.probate.service.solicitorexecutor.ExecutorListMapperService;
import uk.gov.hmcts.probate.service.solicitorexecutor.FormattingService;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LegalStatementExecutorTransformer extends ExecutorsTransformer {

    private final DateFormatterService dateFormatterService;

    public LegalStatementExecutorTransformer(ExecutorListMapperService executorListMapperService,
                                             DateFormatterService dateFormatterService) {
        super(executorListMapperService);
        this.dateFormatterService = dateFormatterService;
    }

    /**
     * Map all executors into executors applying and executors not applying lists for the solicitor legal statement.
     */
    public void mapSolicitorExecutorFieldsToLegalStatementExecutorFields(CaseData caseData) {

        // Create executor lists
        List<CollectionMember<AdditionalExecutorApplying>> execsApplying = createCaseworkerApplyingList(caseData);
        List<CollectionMember<AdditionalExecutorNotApplying>> execsNotApplying =
                createCaseworkerNotApplyingList(caseData);

        createLegalStatementExecutorLists(execsApplying, execsNotApplying, caseData);
    }

    public void createLegalStatementExecutorListsFromTransformedLists(CaseData caseData) {
        List<CollectionMember<AdditionalExecutorApplying>> execsApplying = cloneExecsApplying(caseData);
        List<CollectionMember<AdditionalExecutorNotApplying>> execsNotApplying = cloneExecsNotApplying(caseData);

        createLegalStatementExecutorLists(execsApplying, execsNotApplying, caseData);
    }

    public void createLegalStatementExecutorLists(List<CollectionMember<AdditionalExecutorApplying>> execsApplying,
                                             List<CollectionMember<AdditionalExecutorNotApplying>> execsNotApplying,
                                             CaseData caseData) {



        // Add primary applicant to list
        if (caseData.isPrimaryApplicantApplying()) {
            execsApplying.add(0, executorListMapperService.mapFromPrimaryApplicantToApplyingExecutor(caseData));
        } else if (caseData.isPrimaryApplicantNotApplying()) {
            execsNotApplying.add(0, executorListMapperService
                    .mapFromPrimaryApplicantToNotApplyingExecutor(caseData));
        }

        /* old code - remove once new code tested more thoroughly
            // Add primary applicant to list
            if (isSolicitorExecutor(caseData) && isSolicitorApplying(caseData)) {
                execsApplying.add(executorListMapperService.mapFromSolicitorToApplyingExecutor(caseData));
            } else if (caseData.isPrimaryApplicantApplying()) {
                execsApplying.add(executorListMapperService.mapFromPrimaryApplicantToApplyingExecutor(caseData));
            } else if (caseData.isPrimaryApplicantNotApplying()) {
                execsNotApplying.add(executorListMapperService.mapFromPrimaryApplicantToNotApplyingExecutor(caseData));
            }
         */

        caseData.setExecutorsApplyingLegalStatement(execsApplying);
        caseData.setExecutorsNotApplyingLegalStatement(execsNotApplying);
    }

    public void formatFields(CaseData caseData) {
        formatDates(caseData);
        formatNames(caseData);
        caseData.setSingularProfitSharingTextForLegalStatement(getSingularProfitSharingTextForLegalStatement(caseData));
        caseData.setPluralProfitSharingTextForLegalStatement(getPluralProfitSharingTextForLegalStatement(caseData));
    }

    private void formatDates(CaseData caseData) {
        // Set dispenseWithNoticeLeaveGivenDate format
        if (caseData.getDispenseWithNoticeLeaveGivenDate() != null) {
            caseData.setDispenseWithNoticeLeaveGivenDateFormatted(
                    dateFormatterService.formatDate(caseData.getDispenseWithNoticeLeaveGivenDate()));
        }

        // Set codicilAddedDate format
        if (caseData.getCodicilAddedDateList() != null) {
            List<CollectionMember<String>> formattedCodicilDates = new ArrayList<>();
            caseData.getCodicilAddedDateList().forEach(date -> {
                String formattedDate = dateFormatterService.formatDate(date.getValue().getDateCodicilAdded());
                formattedCodicilDates.add(new CollectionMember<>(formattedDate));
            });
            caseData.setCodicilAddedFormattedDateList(formattedCodicilDates);
        }
    }

    private void formatNames(CaseData caseData) {
        caseData.setDeceasedForenames(FormattingService.capitaliseEachWord(caseData.getDeceasedForenames()));
        caseData.setDeceasedSurname(FormattingService.capitaliseEachWord(caseData.getDeceasedSurname()));
        caseData.setSolsSolicitorFirmName(FormattingService.capitaliseEachWord(caseData.getSolsSolicitorFirmName()));
    }

    private String getSingularProfitSharingTextForLegalStatement(CaseData caseData) {
        return getPluralProfitSharingTextForLegalStatement(caseData, false);
    }

    private String getPluralProfitSharingTextForLegalStatement(CaseData caseData) {
        return getPluralProfitSharingTextForLegalStatement(caseData, true);
    }

    private String getPluralProfitSharingTextForLegalStatement(CaseData caseData, boolean forPlural) {
        if (caseData.getWhoSharesInCompanyProfits() == null) {
            return "";
        }

        String execProfitSharing = "";
        final int len = caseData.getWhoSharesInCompanyProfits().size();
        for (int i = 0; i < len; i++) {
            // lower case and remove the plural 's'
            String whoShares = caseData.getWhoSharesInCompanyProfits().get(i).toLowerCase();
            if (forPlural && !whoShares.endsWith("s")) {
                whoShares += "s";
            } else if (!forPlural && whoShares.endsWith("s")) {
                whoShares = whoShares.substring(0, whoShares.length() - 1);
            }
            execProfitSharing += whoShares;
            if (i < len - 1) {
                execProfitSharing += " and ";
            }
        }
        return execProfitSharing;
    }
}