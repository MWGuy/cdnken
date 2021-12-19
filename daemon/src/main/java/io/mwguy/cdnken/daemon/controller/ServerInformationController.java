package io.mwguy.cdnken.daemon.controller;

import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;
import io.mwguy.cdnken.shared.model.ServerInformationResponse;
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
        var information = new ServerInformationResponse();
        information.setTemplate(configurationProperties.getTemplate());
        information.setCapacity(configurationProperties.getCapacity());
        information.setMirror(configurationProperties.isMirror());

        return information;
    }
}
