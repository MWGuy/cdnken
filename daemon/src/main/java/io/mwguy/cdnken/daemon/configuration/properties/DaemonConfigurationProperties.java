package io.mwguy.cdnken.daemon.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ken.daemon")
public class DaemonConfigurationProperties {
    @NonNull
    private Path directory;

    @NonNull
    private String template;

    @NonNull
    private String token;

    @NonNull
    private Long capacity;

    @NonNull
    private Boolean mirror;
}
