package io.kk.bandhub.venueservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.kk.bandhub.venueservice.config.auth.OAuth2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2Client oAuth2Client;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = oAuth2Client.getAccessToken();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
