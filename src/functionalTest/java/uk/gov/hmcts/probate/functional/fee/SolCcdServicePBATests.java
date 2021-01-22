package uk.gov.hmcts.probate.functional.fee;


import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Pending;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;
import uk.gov.hmcts.probate.functional.util.FunctionalTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
@RunWith(SpringIntegrationSerenityRunner.class)
public class SolCcdServicePBATests extends IntegrationTestBase {

    private static WireMockServer wireMockServer;

    @Autowired
    protected FunctionalTestUtils utils;


    @BeforeClass
    public static void setup() {
        wireMockServer = new WireMockServer(options().port(8991));
        wireMockServer.start();
        wireMockServer.resetRequests();
    }

    @AfterClass
    public static void cleanup() {
        wireMockServer.stop();
    }

    @Before
    public void setupPerTest() {
        stubCreditAccountPayment(utils.getJsonFromFile("pbaWiremockResponses.json"));
    }

    //We are waiting for dat from the ref data team so that this test can run on PR + AAT
    @Test
    @Pending
    public void shouldValidateDefaultPBAs() {
        validatePostRequestSuccessForDefaultingPBAs("solicitorPDFPayloadProbate.json", 
            "{\"code\":\"PBA0087535\",\"label\":\"PBA0087535\"},{\"code\":\"PBA0088063\",\"label\":\"PBA0088063\"}");
    }

    private static void stubCreditAccountPayment(String response) {
        wireMockServer.stubFor(get(urlEqualTo("/refdata/external/v1/organisations/pbas?email=probatesolcw1@gmail.com"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response)));
    }

    private void validatePostRequestSuccessForDefaultingPBAs(String fileName, String expectedValue) {

        String body = given().headers(utils.getHeadersWithCaseworkerUser())
            .relaxedHTTPSValidation()
            .body(utils.getJsonFromFile(fileName))
            .contentType(JSON)
            .when().post("/case/default-sols-pba").getBody().asString();
        assertThat(body, containsString(expectedValue));
    }
}
