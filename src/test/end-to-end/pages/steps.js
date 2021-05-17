'use strict';

const requireDirectory = require('require-directory');
const steps = requireDirectory(module);

module.exports = function () {
    return actor({

        // Login
        authenticateWithIdamIfAvailable: steps.IDAM.signIn,
        selectNewCase: steps.newCase.newCase,
        selectCaseTypeOptions: steps.createCase.createCase,
        handleEvidence: steps.handleEvidence.handleEvidence,
        enterWillLodgementPage1: steps.createWillLodgement.page1,
        enterWillLodgementPage2: steps.createWillLodgement.page2,
        enterWillLodgementPage3: steps.createWillLodgement.page3,
        enterCaveatPage1: steps.createCaveat.page1,
        enterCaveatPage2: steps.createCaveat.page2,
        enterCaveatPage3: steps.createCaveat.page3,
        enterCaveatPage4: steps.createCaveat.page4,
        emailCaveator: steps.emailNotifications.caveat.emailCaveator,
        reopenCaveat: steps.reopenningCases.caveat.reopenCaveat,
        withdrawCaveatPage1: steps.withdrawCaveat.page1,
        enterGrantOfProbatePage1: steps.createGrantOfProbate.page1,
        enterGrantOfProbatePage2: steps.createGrantOfProbate.page2,
        enterGrantOfProbatePage3: steps.createGrantOfProbate.page3,
        enterGrantOfProbatePage4: steps.createGrantOfProbate.page4,
        enterGrantOfProbatePage5: steps.createGrantOfProbate.page5,
        enterGrantOfProbatePage6: steps.createGrantOfProbate.page6,
        enterGrantOfProbatePage7: steps.createGrantOfProbate.page7,
        enterGrantOfProbatePage8: steps.createGrantOfProbate.page8,
        enterGrantOfProbatePage9: steps.createGrantOfProbate.page9,

        caseProgressSolicitorDetails: steps.caseProgress.solicitorDetails,
        caseProgressSolicitorDetailsCheckAnswers: steps.caseProgress.solicitorDetailsCheckAnswers,
        caseProgressDeceasedDetails: steps.caseProgress.deceasedDetails,
        caseProgressDeceasedDetails2: steps.caseProgress.deceasedDetails2,
        caseProgressCheckCaseProgressTab: steps.caseProgress.checkCaseProgressTab,
        caseProgressClickElementsAndContinue: steps.caseProgress.clickElementsAndContinue,
        caseProgressClickSelectOrFillElementsAndContinue: steps.caseProgress.clickSelectOrFillElementsAndContinue,
        caseProgressWaitForElementThenContinue: steps.caseProgress.waitForElementThenContinue,
        caseProgressCheckYourAnswers: steps.caseProgress.checkYourAnswers,
        caseProgressConfirmApplication: steps.caseProgress.confirmApplication,
        caseProgressCompleteApplication: steps.caseProgress.completeApplication,
        caseProgressSubmittedConfirmation: steps.caseProgress.submittedConfirmation,
        caseProgressNavigateToCaseCaseworker: steps.caseProgress.navigateToCaseCaseworker,
        caseProgressCaseworkerChangeState: steps.caseProgress.caseworkerChangeState,
        caseProgressSelectPenultimateNextStepAndGo: steps.caseProgress.selectPenultimateNextStepAndGo,
        caseProgressClickGoAndSignOut: steps.caseProgress.clickGoAndSignOut,
        caseProgressNavigateToCaseSolicitor: steps.caseProgress.navigateToCaseSolicitor,
        caseProgressFeePayment: steps.caseProgress.feePayment,
        caseProgressContinueWithoutChangingAnything: steps.caseProgress.continueWithoutChangingAnything,

        caseProgressAppStoppedDetails: steps.caseProgressAppStopped.appStoppedDetails,
        caseProgressAppStoppedTabCheck: steps.caseProgressAppStopped.appStoppedTabCheck,

        caseProgressStandardDeceasedDetailsCheck: steps.caseProgressStandard.deceasedDetailsCheck,

        caseProgressStopEscalateIssueDeceasedDetailsCheck: steps.caseProgressStopEscalateIssue.deceasedDetailsCheck,
        caseProgressStopEscalateIssueStoppedTabCheck: steps.caseProgressStopEscalateIssue.stoppedTabCheck,
        caseProgressStopEscalateIssueAddCaseStoppedReason: steps.caseProgressStopEscalateIssue.addCaseStoppedReason,
        caseProgressStopEscalateIssueEscalatedTabCheck: steps.caseProgressStopEscalateIssue.escalatedTabCheck,

        checkMyAnswers: steps.checkYourAnswers.checkYourAnswers,
        seeCaseDetails: steps.caseDetails.caseDetails,
        seeUpdatesOnCase: steps.caseDetails.caseDetailsUpdated,
        chooseNextStep: steps.nextStep.nextStep,
        printCase: steps.printCase.printCase,
        enterEventSummary: steps.eventSummary.eventSummary,
        uploadDocument: steps.documentUpload.documentUpload,
        enterComment: steps.eventSummary.eventSummary,
        markForExamination: steps.markForExamination.markForExamination,
        markForIssue: steps.markForIssue.markForIssue,
        issueGrant: steps.issueGrant.issueGrant,
        selectCaseMatchesForWillLodgement: steps.caseMatches.caseMatches,
        selectCaseMatchesForGrantOfProbate: steps.caseMatches.caseMatches,
        selectCaseMatchesForCaveat: steps.caseMatches.caseMatches,
        selectWithdrawalReason: steps.withdrawal.withdrawal,
        applyForProbatePage1: steps.solicitorApplyProbate.applyProbate.page1,
        applyForProbatePage2: steps.solicitorApplyProbate.applyProbate.page2,
        cyaPage: steps.solicitorApplyProbate.cyaPage,
        seeEndState: steps.solicitorApplyProbate.endState,
        deceasedDetailsPage1: steps.solicitorApplyProbate.deceasedDetails.page1,
        deceasedDetailsPage2: steps.solicitorApplyProbate.deceasedDetails.page2,
        deceasedDetailsPage3: steps.solicitorApplyProbate.deceasedDetails.page3,
        deceasedDetailsPage4: steps.solicitorApplyProbate.deceasedDetails.page4,
        grantOfProbatePage1: steps.solicitorApplyProbate.grantOfProbate.page1,
        grantOfProbatePage2: steps.solicitorApplyProbate.grantOfProbate.page2,
        grantOfProbatePage3: steps.solicitorApplyProbate.grantOfProbate.page3,
        completeApplicationPage1: steps.solicitorApplyProbate.completeApplication.page1,
        completeApplicationPage2: steps.solicitorApplyProbate.completeApplication.page2,
        completeApplicationPage2a: steps.solicitorApplyProbate.completeApplication.page2a,
        completeApplicationPage3: steps.solicitorApplyProbate.completeApplication.page3,
        completeApplicationPage4: steps.solicitorApplyProbate.completeApplication.page4,
        completeApplicationPage5: steps.solicitorApplyProbate.completeApplication.page5,
        completeApplicationPage6: steps.solicitorApplyProbate.completeApplication.page6,
        completeApplicationPage7: steps.solicitorApplyProbate.completeApplication.page7,
        admonWillDetailsPage1: steps.solicitorApplyProbate.admonWillDetails.page1,
        admonWillDetailsPage2: steps.solicitorApplyProbate.admonWillDetails.page2,
        admonWillDetailsPage3: steps.solicitorApplyProbate.admonWillDetails.page3,
        admonWillDetailsPage4: steps.solicitorApplyProbate.admonWillDetails.page4,
        intestacyDetailsPage1: steps.solicitorApplyProbate.intestacyDetails.page1,
        intestacyDetailsPage2: steps.solicitorApplyProbate.intestacyDetails.page2,
        intestacyDetailsPage3: steps.solicitorApplyProbate.intestacyDetails.page3,
        applyCaveatPage1: steps.solicitorApplyCaveat.applyCaveat.page1,
        applyCaveatPage2: steps.solicitorApplyCaveat.applyCaveat.page2,
        caveatApplicationDetailsPage1: steps.solicitorApplyCaveat.applicationDetails.page1,
        caveatApplicationDetailsPage2: steps.solicitorApplyCaveat.applicationDetails.page2,
        completeCaveatApplicationPage1: steps.solicitorApplyCaveat.completeApplication.page1,
        completeCaveatApplicationPage2: steps.solicitorApplyCaveat.completeApplication.page2,
        completeCaveatApplicationPage3: steps.solicitorApplyCaveat.completeApplication.page3,
        getCaseRefFromUrl: steps.utility.getCaseRefFromUrl,
        logPageHtml: steps.utility.logPageHtml
    });
};
