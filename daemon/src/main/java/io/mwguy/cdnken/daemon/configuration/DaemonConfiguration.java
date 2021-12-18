package io.mwguy.cdnken.daemon.configuration;

import io.mwguy.cdnken.daemon.component.StoragePathResolverImpl;
import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DaemonConfigurationProperties.class)
public class DaemonConfiguration {
    private final DaemonConfigurationProperties configurationProperties;

    public DaemonConfiguration(DaemonConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
        // TODO: check properties
    }

    public StoragePathResolverImpl storagePathResolver() {
        return new StoragePathResolverImpl(configurationProperties);
    }
}
