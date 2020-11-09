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
        enterGrantOfProbatePage1: steps.createGrantOfProbate.page1,
        enterGrantOfProbatePage2: steps.createGrantOfProbate.page2,
        enterGrantOfProbatePage3: steps.createGrantOfProbate.page3,
        enterGrantOfProbatePage4: steps.createGrantOfProbate.page4,
        enterGrantOfProbatePage5: steps.createGrantOfProbate.page5,
        enterGrantOfProbatePage6: steps.createGrantOfProbate.page6,
        enterGrantOfProbatePage7: steps.createGrantOfProbate.page7,
        enterGrantOfProbatePage8: steps.createGrantOfProbate.page8,
        enterGrantOfProbatePage9: steps.createGrantOfProbate.page9,
        caseProgressPage1: steps.caseProgress.page1,
        caseProgressPage2: steps.caseProgress.page2,
        caseProgressPage3: steps.caseProgress.page3,
        caseProgressPage4: steps.caseProgress.page4,
        caseProgressPage5: steps.caseProgress.page5,
        caseProgressPage6: steps.caseProgress.page6,
        caseProgressPage7: steps.caseProgress.page7,
        caseProgressPage8: steps.caseProgress.page8,   
        caseProgressPage9: steps.caseProgress.page9,         
        caseProgressPage10: steps.caseProgress.page10,         
        caseProgressPage11: steps.caseProgress.page11,         
        caseProgressPage12: steps.caseProgress.page12,         
        caseProgressPage13: steps.caseProgress.page13,         
        caseProgressPage14: steps.caseProgress.page14,         
        caseProgressPage15: steps.caseProgress.page15,  
        caseProgressPage16: steps.caseProgress.page16,  
        caseProgressPage17: steps.caseProgress.page17,  
        checkMyAnswers: steps.checkYourAnswers.checkYourAnswers,
        seeCaseDetails: steps.caseDetails.caseDetails,
        chooseNextStep: steps.nextStep.nextStep,
        printCase: steps.printCase.printCase,
        enterEventSummary: steps.eventSummary.eventSummary,
        uploadDocument: steps.documentUpload.documentUpload,
        enterComment: steps.eventSummary.eventSummary,
        markForExamination: steps.markForExamination.markForExamination,
        markForIssue: steps.markForIssue.markForIssue,
        issueGrant: steps.issueGrant.issueGrant,
        selectCaseMatchesForWillLodgement: steps.caseMatches.willLodgement.caseMatches,
        selectCaseMatchesForGrantOfProbate: steps.caseMatches.grantOfProbate.caseMatches,
        selectCaseMatchesForCaveat: steps.caseMatches.caveat.caseMatches,
        selectWithdrawalReason: steps.withdrawal.withdrawal
    });
};
