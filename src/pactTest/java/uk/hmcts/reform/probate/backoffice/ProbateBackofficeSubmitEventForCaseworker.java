package uk.hmcts.reform.probate.backoffice;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import java.util.Map;
import org.apache.http.client.fluent.Executor;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.hmcts.reform.probate.pact.dsl.PactDslBuilderForCaseDetailsList.buildCaseDetailsDsl;
import static uk.hmcts.reform.probate.backoffice.util.AssertionHelper.assertCaseDetails;
import static uk.hmcts.reform.probate.backoffice.util.ObjectMapperTestUtil.convertObjectToJsonString;
import static uk.hmcts.reform.probate.backoffice.util.PactDslFixtureHelper.getCaseDataContent;

public class ProbateBackofficeSubmitEventForCaseworker extends AbstractBackOfficePact {

    public static final String SOME_AUTHORIZATION_TOKEN = "Bearer UserAuthToken";
    public static final String SOME_SERVICE_AUTHORIZATION_TOKEN = "ServiceToken";

    @Value("${ccd.jurisdictionid}")
    String jurisdictionId;

    @Value("${ccd.casetype}")
    String caseType;

    @Value("${ccd.eventid.create}")
    String createEventId;

    private Map<String, Object> caseDetailsMap;

    private static final String CASE_ID = "2000";
    private CaseDataContent caseDataContent;
    private static final String USER_ID = "123456";
    private static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @BeforeEach
    public void setUpEachTest() throws InterruptedException {
        Thread.sleep(2000);
    }

    @After
    public void teardown() {
        Executor.closeIdleConnections();
    }

    @Pact(provider = "ccdDataStoreAPI_Cases", consumer = "probate_backOfficeService")
    RequestResponsePact submitEventForCaseWorker(PactDslWithProvider builder) throws Exception {
        // @formatter:off
        return builder
                .given("A SubmitEvent for Caseworker is requested", getCaseDataContentAsMap(caseDataContent))
                .uponReceiving("A Submit Event for a Caseworker")
                .path("/caseworkers/"   + USER_ID
                        + "/jurisdictions/" + jurisdictionId
                        + "/case-types/"    + caseType
                        + "/cases/"         + CASE_ID
                        + "/events"
                )
                .query("ignore-warning=true")
                .method("POST")
                .headers(HttpHeaders.AUTHORIZATION, SOME_AUTHORIZATION_TOKEN, SERVICE_AUTHORIZATION, SOME_SERVICE_AUTHORIZATION_TOKEN)
                .body(convertObjectToJsonString(getCaseDataContent()))
                .matchHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .willRespondWith()
                .status(200)
                .body(buildCaseDetailsDsl(100L, "someemailaddress.com", false, false, false))
                .matchHeader(HttpHeaders.CONTENT_TYPE, "\\w+\\/[-+.\\w]+;charset=(utf|UTF)-8")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "submitEventForCaseWorker")
    public void verifySubmitEventForCaseworker() throws Exception {

        final CaseDetails caseDetails = coreCaseDataApi.submitEventForCaseWorker(SOME_AUTHORIZATION_TOKEN,
                SOME_SERVICE_AUTHORIZATION_TOKEN, USER_ID, jurisdictionId, caseType, CASE_ID,true,caseDataContent);

        assertThat(caseDetails.getId(), is(100L));
        assertThat(caseDetails.getJurisdiction(), is("DIVORCE"));

        assertCaseDetails(caseDetails,false,false);
    }

}
