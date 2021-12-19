package io.mwguy.cdnken.shared.component;

import io.mwguy.cdnken.shared.model.SimpleAuthenticationImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.util.function.Supplier;

public class CustomAuthenticationManager implements AuthenticationManager {
    private final Supplier<String> tokenSupplier;

    public CustomAuthenticationManager(Supplier<String> tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof BearerTokenAuthenticationToken) {
            var token = (BearerTokenAuthenticationToken) authentication;
            if (!tokenSupplier.get().equals(token.getToken())) {
                throw new InvalidBearerTokenException("Invalid token");
            }

            return new SimpleAuthenticationImpl();
        }

        throw new AuthenticationServiceException("Unimplemented");
    }
}
