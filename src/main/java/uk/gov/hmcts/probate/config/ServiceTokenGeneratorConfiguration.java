package uk.gov.hmcts.probate.config;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;

@Configuration
public class ServiceTokenGeneratorConfiguration {
    @Bean
    public ServiceAuthTokenGenerator serviceAuthTokenGenerator(
        @Value("${idam.s2s-auth.url}") String s2sUrl,
        @Value("${idam.s2s-auth.totp_secret}") String secret,
        @Value("${idam.s2s-auth.microservice}") String microservice) {

        final ServiceAuthorisationApi serviceAuthorisationApi = Feign.builder()
            .encoder(new JacksonEncoder())
            .contract(new SpringMvcContract())
            .target(ServiceAuthorisationApi.class, s2sUrl);

        return new ServiceAuthTokenGenerator(secret, microservice, serviceAuthorisationApi);
    }
}