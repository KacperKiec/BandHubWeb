package io.kk.bandhub.eventservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.kk.bandhub.eventservice.service.OAuth2Client;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2Client oAuth2Client;

    public FeignRequestInterceptor(OAuth2Client oAuth2Client) {
        this.oAuth2Client = oAuth2Client;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = oAuth2Client.getAccessToken();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}