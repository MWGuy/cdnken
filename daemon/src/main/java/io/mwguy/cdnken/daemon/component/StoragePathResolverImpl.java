package io.mwguy.cdnken.daemon.component;

import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

public class StoragePathResolverImpl implements StoragePathResolver {
    private final DaemonConfigurationProperties configurationProperties;

    public StoragePathResolverImpl(DaemonConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public Path resolve(String name) {
        if (name.contains("..")) {
            throw new IllegalArgumentException("Bad object name");
        }

        return configurationProperties.getDirectory().resolve(name);
    }
}
