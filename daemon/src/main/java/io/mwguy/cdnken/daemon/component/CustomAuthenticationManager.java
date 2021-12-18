package io.mwguy.cdnken.daemon.component;

import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;
import io.mwguy.cdnken.daemon.model.DaemonAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    private final DaemonConfigurationProperties configurationProperties;

    public CustomAuthenticationManager(DaemonConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof BearerTokenAuthenticationToken) {
            var token = (BearerTokenAuthenticationToken) authentication;
            if (!configurationProperties.getToken().equals(token.getToken())) {
                throw new InvalidBearerTokenException("Invalid token");
            }

            return new DaemonAuthentication();
        }

        throw new AuthenticationServiceException("Unimplemented");
    }
}
