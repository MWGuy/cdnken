package io.mwguy.cdnken.daemon.configuration;

import io.mwguy.cdnken.daemon.component.StoragePathResolverImpl;
import io.mwguy.cdnken.daemon.configuration.properties.DaemonConfigurationProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.file.Files;

@Slf4j
@Configuration
@EnableConfigurationProperties(DaemonConfigurationProperties.class)
public class DaemonConfiguration {
    private final DaemonConfigurationProperties configurationProperties;

    public DaemonConfiguration(DaemonConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @SneakyThrows
    @PostConstruct
    public void checkConfiguration() {
        var token = configurationProperties.getToken();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token can't be empty");
        }

        var path = configurationProperties.getDirectory().toAbsolutePath();
        if (Files.exists(path) && !Files.isDirectory(path)) {
            throw new RuntimeException("{} - is not directory");
        }

        if (!Files.exists(path)) {
            log.warn("Directory '{}' not exists, creating", path);
            Files.createDirectories(path);
        }

        if (configurationProperties.getCapacity() == 0) {
            log.warn("Capacity not set, using getFreeSpace()");
            configurationProperties.setCapacity(path.toFile().getFreeSpace());
        }
    }

    @Bean
    public StoragePathResolverImpl storagePathResolver() {
        return new StoragePathResolverImpl(configurationProperties);
    }
}
