package uk.gov.hmcts.probate.service.payments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.probate.exception.BusinessValidationException;
import uk.gov.hmcts.probate.model.payments.CreditAccountPayment;
import uk.gov.hmcts.probate.model.payments.PaymentResponse;
import uk.gov.hmcts.probate.service.BusinessValidationMessageRetriever;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.net.URI;
import java.util.Locale;
import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsService {

    private static final String PAYMENT_ERROR_404 = "Account information could not be found";
    private static final String PAYMENT_ERROR_422 = "Invalid or missing attribute";
    private static final String PAYMENT_ERROR_400 = "Payment Failed";
    private static final String PAYMENT_ERROR_5XX = "Unable to retrieve account information, please try again later";
    private static final String PAYMENT_ERROR_OTHER = "Unexpected Exception";
    private final RestTemplate restTemplate;
    private final AuthTokenGenerator authTokenGenerator;
    private final BusinessValidationMessageRetriever businessValidationMessageRetriever;

    @Value("${payment.url}")
    private String payUri;
    @Value("${payment.api}")
    private String payApi;

    public PaymentResponse getCreditAccountPaymentResponse(String authToken,
                                                           CreditAccountPayment creditAccountPayment) {
        URI uri = fromHttpUrl(payUri + payApi).build().toUri();
        HttpEntity<CreditAccountPayment> request = buildRequest(authToken, creditAccountPayment);

        log.info("PaymentService.getCreditAccountPaymentResponse uri:" + uri);
        PaymentResponse paymentResponse = null;
        try {
            ResponseEntity<PaymentResponse> responseEntity = restTemplate.exchange(uri, POST,
                request, PaymentResponse.class);
            paymentResponse = Objects.requireNonNull(responseEntity.getBody());
            log.info("paymentResponse : {}", paymentResponse);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == FORBIDDEN.value()) {
                throw getNewBusinessValidationException(e);
            } else if (e.getStatusCode().value() == NOT_FOUND.value()) {
                throw new BusinessValidationException(PAYMENT_ERROR_404, e.getMessage());
            } else if (e.getStatusCode().value() == UNPROCESSABLE_ENTITY.value()) {
                throw new BusinessValidationException(PAYMENT_ERROR_422, e.getMessage());
            } else if (e.getStatusCode().value() == BAD_REQUEST.value()) {
                throw new BusinessValidationException(PAYMENT_ERROR_400, e.getMessage());
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new BusinessValidationException(PAYMENT_ERROR_5XX, e.getMessage());
            } else {
                throw new BusinessValidationException(PAYMENT_ERROR_OTHER, e.getMessage());
            }
        }
        return paymentResponse;
    }

    private BusinessValidationException getNewBusinessValidationException(HttpClientErrorException e) {
        String[] payError = {getErrorMessage(e)};
        String error1 = businessValidationMessageRetriever.getMessage("creditAccountPaymentErrorMessage", payError,
            Locale.UK);
        String[] empty = {};
        String error2 = businessValidationMessageRetriever.getMessage("creditAccountPaymentErrorMessage2",
            empty, Locale.UK);
        String error3 = businessValidationMessageRetriever.getMessage("creditAccountPaymentErrorMessage3",
            empty, Locale.UK);
        String error4 = businessValidationMessageRetriever.getMessage("creditAccountPaymentErrorMessage4",
            empty, Locale.UK);
        String error5 = businessValidationMessageRetriever.getMessage("creditAccountPaymentErrorMessage5",
            empty, Locale.UK);
        return new BusinessValidationException(error1, e.getMessage(), error2, error3,
            error4, error5);
    }

    private String getErrorMessage(HttpClientErrorException e) {

        String body = e.getResponseBodyAsString();
        log.info("getErrorMessage.body:" + body);
        JSONObject json = new JSONObject(body);
        JSONArray jsonArray = json.getJSONArray("status_histories");
        String statusHistory = jsonArray.get(0).toString();
        json = new JSONObject(statusHistory);
        return json.getString("error_message");
    }

    private HttpEntity<CreditAccountPayment> buildRequest(String authToken, CreditAccountPayment creditAccountPayment) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authToken);
        headers.add("Content-Type", "application/json");
        String sa = authTokenGenerator.generate();
        headers.add("ServiceAuthorization", sa);

        return new HttpEntity<>(creditAccountPayment, headers);
    }

}