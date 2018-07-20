package uk.gov.hmcts.probate.functional.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestCaseCreatorConfig.class)
public class TestCaseCreator {

    {
        System.setProperty("socksProxyHost", "localhost");
        System.setProperty("socksProxyPort", "9090");
    }

    private String clientToken;

    private String userId;

    @Value("${user.auth.provider.oauth2.url}")
    private String idamUserBaseUrl;

    @Value("${ccd.data.store.api.url}")
    private String solCcdServiceUrl;

    @Value("${idam.secret}")
    private String idamSecret;

    @Value("${idam.username}")
    private String idamUsername;

    @Value("${idam.userpassword}")
    private String idamPassword;

    @Value("${idam.oauth2.redirect_uri}")
    private String redirectUri;

    @Autowired
    private RelaxedServiceAuthTokenGenerator relaxedServiceAuthTokenGenerator;

    @Rule
    public SpringIntegration springIntegration;

    private ObjectMapper objectMapper = new ObjectMapper();

    public TestCaseCreator() {
        this.springIntegration = new SpringIntegration();

    }

    @Before
    public void setUp() {
        RestAssured.baseURI = solCcdServiceUrl;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Ignore
    @Test
    public void createPaCase() throws Exception {
        createCase("create.pa.ccd.json", "citizens", "applyForGrant");
    }

    @Ignore
    @Test
    public void createSolsCase() throws Exception {
        createCase("create.sols.ccd.json", "caseworkers", "solicitorCreateApplication");
    }

    private void createCase(String fileName, String role, String eventName) throws Exception {
        Headers headersWithUserId = getHeadersWithUserId();
        userId = getUserId(clientToken);
        String token = generateEventToken(role, eventName, headersWithUserId);
        String rep = getJsonFromFile(fileName).replace("\"event_token\": \"sampletoken\"", "\"event_token\":\"" + token + "\"");


        SerenityRest.given()
                .relaxedHTTPSValidation()
                .headers(headersWithUserId)
                .baseUri(solCcdServiceUrl)
                .body(rep)
                .when().post("/" + role + "/" + userId + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/cases").
                        then()
                .statusCode(201);
    }

    public Headers getHeadersWithUserId() throws Exception {
        return getHeadersWithUserId(generateServiceToken());
    }


    public String generateServiceToken() {
        String serviceToken = relaxedServiceAuthTokenGenerator.generate();
        log.info("Service Token: {}", serviceToken);
        return serviceToken;
    }

    public Headers getHeadersWithUserId(String serviceToken) throws Exception {
        return Headers.headers(
                new Header("ServiceAuthorization", serviceToken),
                new Header("Content-Type", ContentType.JSON.toString()),
                new Header("Authorization", generateUserTokenWithNoRoles()));
    }

    private String generateEventToken(String role, String eventName, Headers headersWithUserId) {
        log.info("User Id: {}", userId);
        RestAssured.baseURI = solCcdServiceUrl;
        return SerenityRest.given()
                .relaxedHTTPSValidation()
                .headers(headersWithUserId)
                .when().get("/" + role + "/" + userId + "/jurisdictions/PROBATE/case-types/GrantOfRepresentation/event-triggers/" + eventName + "/token")
                .then().assertThat().statusCode(200).extract().path("token");
    }


    public String generateUserTokenWithNoRoles() throws Exception {
        clientToken = generateClientToken();
        log.info("Client Token : {}", clientToken);
        return clientToken;
    }

    private String generateClientToken() throws Exception {
        String code = generateClientCode();
        log.info("Client Code: {}", code);
        return "Bearer " + RestAssured.given().relaxedHTTPSValidation().post(idamUserBaseUrl + "/oauth2/token?code=" + code +
                "&client_secret=" + idamSecret +
                "&client_id=probate" +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code")
                .body().path("access_token");
    }

    private String generateClientCode() throws Exception {
        final String encoded = Base64.getEncoder().encodeToString((idamUsername + ":" + idamPassword).getBytes());
        JsonNode jsonNode = objectMapper.readValue(RestAssured.given().relaxedHTTPSValidation().baseUri(idamUserBaseUrl)
                .header("Authorization", "Basic " + encoded)
                .post("/oauth2/authorize?response_type=code&client_id=probate&redirect_uri=" + redirectUri)
                .body().print(), JsonNode.class);
        return jsonNode.get("code").asText();
    }

    private String getJsonFromFile(String fileName) {
        try {
            File file = ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserId(String userToken) {
        return "" + RestAssured.given()
                .header("Authorization", userToken)
                .get(idamUserBaseUrl + "/details")
                .body()
                .path("id");
    }
}
