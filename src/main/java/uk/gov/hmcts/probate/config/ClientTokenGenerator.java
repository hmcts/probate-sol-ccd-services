package uk.gov.hmcts.probate.config;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Configuration
public class ClientTokenGenerator {

    @Value("${auth.provider.client.user}")
    private String idamUrl;

    @Value("${auth.provider.client.secret}")
    private String secret;

    @Value("${auth.provider.client.id}")
    private String id;

    @Value("${auth.provider.client.redirect}")
    private String redirect;

    @Value("${auth.provider.client.email}")
    private String email;

    @Value("${auth.provider.client.password}")
    private String password;

    private HttpClient client = HttpClients.createDefault();

    @Bean
    public String generateClientToken() throws IOException {
        String code = generateClientCode();

        HttpPost post = new HttpPost(idamUrl + "/oauth2/token");
        post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("code", code));
        urlParameters.add(new BasicNameValuePair("client_secret", secret));
        urlParameters.add(new BasicNameValuePair("client_id", id));
        urlParameters.add(new BasicNameValuePair("redirect_uri", redirect));
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        StringBuilder result = getResponse(response);

        JSONObject jsonObject = new JSONObject(result.toString());

        return jsonObject.get("access_token").toString();

    }

    private String generateClientCode() throws IOException {
        final String encoded = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        HttpPost post = new HttpPost(idamUrl + "/oauth2/authorize?response_type=code&client_id=" + id +
                "&redirect_uri=" + redirect);
        post.setHeader("Authorization", "Basic " + encoded);

        HttpResponse response = client.execute(post);

        StringBuilder result = getResponse(response);

        JSONObject jsonObject = new JSONObject(result.toString());

        return jsonObject.get("code").toString();
    }

    private StringBuilder getResponse(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }
}
