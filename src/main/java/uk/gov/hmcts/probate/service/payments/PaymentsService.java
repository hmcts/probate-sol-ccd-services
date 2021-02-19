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
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.net.URI;
import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsService {

    private final RestTemplate restTemplate;
    private final AuthTokenGenerator authTokenGenerator;
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
        ResponseEntity<PaymentResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri, POST,
                request, PaymentResponse.class);
            paymentResponse = Objects.requireNonNull(responseEntity.getBody());
            log.info("paymentResponse : {}", paymentResponse);
        } catch (HttpClientErrorException e) {
            throw new BusinessValidationException("PBA payment failed: " + getErrorMessage(e), e.getMessage());
        }
        return paymentResponse;
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