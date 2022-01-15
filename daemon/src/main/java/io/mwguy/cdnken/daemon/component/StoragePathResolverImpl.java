package io.mwguy.cdnken.daemon.component;

import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;

import java.io.IOException;
import java.nio.file.Path;

public class StoragePathResolverImpl implements StoragePathResolver {
    private final DaemonConfigurationProperties configurationProperties;

    public StoragePathResolverImpl(DaemonConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public Path resolve(String name) throws IOException {
        if (name.contains("..") || name.startsWith("/") || name.startsWith(".")) {
            throw new IOException("Bad object name");
        }

        return configurationProperties.getDirectory().resolve(name);
    }
}
