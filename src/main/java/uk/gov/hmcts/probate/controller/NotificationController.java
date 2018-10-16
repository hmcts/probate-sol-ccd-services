package uk.gov.hmcts.probate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.model.ccd.raw.Document;
import uk.gov.hmcts.probate.model.ccd.raw.request.CallbackRequest;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseData;
import uk.gov.hmcts.probate.model.ccd.raw.request.CaseDetails;
import uk.gov.hmcts.probate.model.ccd.raw.response.CallbackResponse;
import uk.gov.hmcts.probate.service.NotificationService;
import uk.gov.hmcts.probate.transformer.CallbackResponseTransformer;
import uk.gov.service.notify.NotificationClientException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.probate.model.State.CASE_STOPPED;
import static uk.gov.hmcts.probate.model.State.DOCUMENTS_RECEIVED;

@RequiredArgsConstructor
@RequestMapping(value = "/notify", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_VALUE)
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final CallbackResponseTransformer callbackResponseTransformer;

    @PostMapping(path = "/documents-received")
    public ResponseEntity<CallbackResponse> sendDocumentReceivedNotification(@RequestBody CallbackRequest callbackRequest)
            throws NotificationClientException {
        CaseDetails caseDetails = callbackRequest.getCaseDetails();
        CaseData caseData = callbackRequest.getCaseDetails().getData();

        List<Document> documents = new ArrayList<>();

        if (caseData.isDocsReceivedEmailNotificationRequested()) {
            Document documentsReceivedSentEmail = notificationService.sendEmail(DOCUMENTS_RECEIVED, caseDetails);
            documents.add(documentsReceivedSentEmail);
        }

        return ResponseEntity.ok(callbackResponseTransformer.addDocuments(callbackRequest, documents));
    }

    @PostMapping(path = "/case-stopped")
    public ResponseEntity<CallbackResponse> sendCaseStoppedNotification(@RequestBody CallbackRequest callbackRequest)
            throws NotificationClientException {
        CaseDetails caseDetails = callbackRequest.getCaseDetails();

        Document document = notificationService.sendEmail(CASE_STOPPED, caseDetails);

        return ResponseEntity.ok(callbackResponseTransformer.caseStopped(callbackRequest, document));
    }
}
