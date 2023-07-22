package au.com.jc.authservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthServiceController {

    // Inject the OAuth authorized client service and authorized client manager
    // from the OAuthClientConfiguration class
    @Autowired
    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    @GetMapping(path = "/access_token")
    public ResponseEntity<?> generateAccessToken() {
        // Build an OAuth2 request for the Okta provider
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("okta")
                .principal("jc-auth-service")
                .build();

        // Perform the actual authorization request using the authorized client service and authorized client
        // manager. This is where the JWT is retrieved from the Okta servers.
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);

        // Get the token from the authorized client object
        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

        log.debug("Issued: {}, Expires: {}", accessToken.getIssuedAt(), accessToken.getExpiresAt());
        log.debug("Scopes: {}", accessToken.getScopes());
        log.debug("Token: {}", accessToken.getTokenValue());

        return ResponseEntity.ok(accessToken);
    }
}
