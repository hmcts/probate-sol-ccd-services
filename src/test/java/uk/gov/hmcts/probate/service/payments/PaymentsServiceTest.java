package uk.gov.hmcts.probate.service.payments;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.probate.exception.BusinessValidationException;
import uk.gov.hmcts.probate.model.payments.CreditAccountPayment;
import uk.gov.hmcts.probate.model.payments.PaymentResponse;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentsServiceTest {
    @MockBean(name = "restTemplate")
    private RestTemplate restTemplate;
    @MockBean
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private PaymentsService paymentsService;

    @MockBean
    private CreditAccountPayment creditAccountPayment;

    @Mock
    private PaymentResponse paymentResponse;

    @Mock
    private HttpClientErrorException httpClientErrorExceptionMock;

    private static final String AUTH_TOKEN = "Bearer .AUTH";

    @Test
    public void shouldGetPaymentResponse() {
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class),
            any(HttpEntity.class), any(Class.class))).thenReturn(ResponseEntity.of(Optional.of(paymentResponse)));

        PaymentResponse returnedPaymentResponse = paymentsService.getCreditAccountPaymentResponse(AUTH_TOKEN,
            creditAccountPayment);

        assertEquals(paymentResponse, returnedPaymentResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldGetExceptionOnNullPaymentResponse() {
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class),
            any(HttpEntity.class), any(Class.class))).thenReturn(ResponseEntity.of(Optional.empty()));

        paymentsService.getCreditAccountPaymentResponse(AUTH_TOKEN, creditAccountPayment);
    }

    @Test(expected = BusinessValidationException.class)
    public void shouldFailOnAccountDeleted() {
        String body = "{\"reference\":\"RC-1599-4778-4711-5958\",\"date_created\":\"2020-09-07T11:24:07.160+0000\"," 
            + "\"status\":\"failed\",\"payment_group_reference\":\"2020-1599477846961\","
            + "\"status_histories\":[{\"status\":\"failed\",\"error_code\":\"CA-E0004\",\"error_message\":\"Your "
            + "account is deleted\",\"date_created\":\"2020-09-07T11:24:07.169+0000\","
            + "\"date_updated\":\"2020-09-07T11:24:07.169+0000\"}]}";
        when(httpClientErrorExceptionMock.getResponseBodyAsString()).thenReturn(body);
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class),
            any(HttpEntity.class), any(Class.class))).thenThrow(httpClientErrorExceptionMock);

        paymentsService.getCreditAccountPaymentResponse("Bearer .123", creditAccountPayment);
    }

}