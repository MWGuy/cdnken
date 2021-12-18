package io.mwguy.cdnken.daemon.controller;

import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;
import io.mwguy.cdnken.daemon.model.ServerInformationResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServerInformationController {
    private final DaemonConfigurationProperties configurationProperties;

    public ServerInformationController(DaemonConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @GetMapping("/info")
    @PreAuthorize("isFullyAuthenticated()")
    public ServerInformationResponse serverInformation() {
        return ServerInformationResponse.builder()
                .template(configurationProperties.getTemplate())
                .capacity(configurationProperties.getCapacity())
                .mirror(configurationProperties.getMirror())
                .build();
    }
}
