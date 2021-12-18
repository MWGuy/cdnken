package io.mwguy.cdnken.daemon.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ken.daemon")
public class DaemonConfigurationProperties {
    private Path directory;
    private String template;
    private String token;
    private long capacity;
    private boolean mirror;
}
