package uk.gov.hmcts.probate.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.exception.model.FieldErrorResponse;
import uk.gov.hmcts.probate.model.GrantDelayedResponse;
import uk.gov.hmcts.probate.model.ccd.CCDData;
import uk.gov.hmcts.probate.model.ccd.CcdCaseType;
import uk.gov.hmcts.probate.model.ccd.EventId;
import uk.gov.hmcts.probate.model.ccd.raw.CollectionMember;
import uk.gov.hmcts.probate.model.ccd.raw.Document;
import uk.gov.hmcts.probate.model.ccd.raw.request.ReturnedCaseDetails;
import uk.gov.hmcts.probate.security.SecurityUtils;
import uk.gov.hmcts.probate.service.ccd.CcdClientApi;
import uk.gov.hmcts.probate.validator.EmailAddressNotifyApplicantValidationRule;
import uk.gov.hmcts.reform.probate.model.ProbateDocument;
import uk.gov.hmcts.reform.probate.model.ProbateDocumentLink;
import uk.gov.hmcts.reform.probate.model.ProbateDocumentType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.service.notify.NotificationClientException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrantDelayedNotificationService {

    private final NotificationService notificationService;
    private final EmailAddressNotifyApplicantValidationRule emailAddressNotifyApplicantValidationRule;
    private final CaseQueryService caseQueryService;
    private final CcdClientApi ccdClientApi;
    private final SecurityUtils securityUtils;

    public GrantDelayedResponse handleGrantDelayedNotification(String date) {
        List<String> delayedRepsonseData = new ArrayList<>();
        List<ReturnedCaseDetails> foundCases = caseQueryService.findCasesForGrantDelayed(date);
        log.info("Found cases for grant delayed notification: {}", foundCases.size());
        for (ReturnedCaseDetails foundCase : foundCases) {
            delayedRepsonseData.add(sendNotificationForCase(foundCase));
        }
        return GrantDelayedResponse.builder().delayResponseData(delayedRepsonseData).build();
    }

    private String sendNotificationForCase(ReturnedCaseDetails foundCase) {
        log.info("Preparing to send email to executors for grant delayed notification");
        CCDData dataForEmailAddress = CCDData.builder()
            .primaryApplicantEmailAddress(foundCase.getData().getPrimaryApplicantEmailAddress())
            .applicationType(foundCase.getData().getApplicationType().getCode())
            .build();
        List<FieldErrorResponse> emailErrors = emailAddressNotifyApplicantValidationRule.validate(dataForEmailAddress);
        String caseId = foundCase.getId().toString();
        if (!emailErrors.isEmpty()) {
            log.error("Cannot send Grant Delayed notification, message: {}", emailErrors.get(0).getMessage());
            return getErroredCaseIdentifier(caseId, emailErrors.get(0).getMessage());
        }
        try {
            Document emailDocument = notificationService.sendGrantDelayedEmail(foundCase);
            updateFoundCase(foundCase, emailDocument);
        } catch (NotificationClientException e) {
            log.error("Error sending email for Grant Delayed with exception: {}. Has message: {}", e.getClass(), e.getMessage());
            caseId = getErroredCaseIdentifier(caseId, e.getMessage());
        } catch (RuntimeException re) {
            log.error("Error updating case for Grant Delayed with exception: {}. Has message: {}", re.getClass(), re.getMessage());
            caseId = getErroredCaseIdentifier(caseId, re.getMessage());
        }

        return caseId;
    }

    private String getErroredCaseIdentifier(String caseId, String message) {
        return "<" + caseId + ":" + message +">";
    }

    private void updateFoundCase(ReturnedCaseDetails foundCase, Document emailDocument) {
        log.info("Updating case for grant delayed, caseId: {}", foundCase.getId());
        
        GrantOfRepresentationData grantOfRepresentationData = GrantOfRepresentationData.builder()
            .grantDelayedNotificationSent(Boolean.TRUE)
            .probateNotificationsGenerated(getProbateDocuments(emailDocument, foundCase.getData().getProbateDocumentsGenerated())) 
            .build();
        ccdClientApi.updateCaseAsCaseworker(CcdCaseType.GRANT_OF_REPRESENTATION, foundCase.getId().toString(),
            grantOfRepresentationData, EventId.UPDATE_GRANT_DELAY_NOTIFICATION_SENT, securityUtils.getSecurityDTO());

    }

    private List<uk.gov.hmcts.reform.probate.model.cases.CollectionMember<ProbateDocument>> getProbateDocuments(Document emailDocument, 
                                                                        List<CollectionMember<Document>> probateDocumentsGenerated) {
        List<uk.gov.hmcts.reform.probate.model.cases.CollectionMember<ProbateDocument>> probateDocuments = new ArrayList<>();
        for (CollectionMember<Document> documentCollectionMember : probateDocumentsGenerated) {
            probateDocuments.add(new uk.gov.hmcts.reform.probate.model.cases.CollectionMember<ProbateDocument>(documentCollectionMember.getId(), 
                getProbateDocument(documentCollectionMember.getValue())));

        }
        probateDocuments.add(new uk.gov.hmcts.reform.probate.model.cases.CollectionMember<>(null, getProbateDocument(emailDocument)));
        return probateDocuments;
    }

    private ProbateDocument getProbateDocument(Document boDocument) {
        ProbateDocumentLink probateDocumentLink = ProbateDocumentLink.builder()
            .documentBinaryUrl(boDocument.getDocumentLink().getDocumentBinaryUrl())
            .documentFilename(boDocument.getDocumentLink().getDocumentFilename())
            .documentUrl(boDocument.getDocumentLink().getDocumentUrl())
            .build();
        ProbateDocumentType probateDocumentType = ProbateDocumentType.valueOf(boDocument.getDocumentType().name());
        return ProbateDocument.builder()
            .documentDateAdded(boDocument.getDocumentDateAdded())
            .documentFileName(boDocument.getDocumentFileName())
            .documentGeneratedBy(boDocument.getDocumentGeneratedBy())
            .documentLink(probateDocumentLink)
            .documentType(probateDocumentType)
            .build();

    }
}
