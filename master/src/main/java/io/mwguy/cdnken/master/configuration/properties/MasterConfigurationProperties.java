package io.mwguy.cdnken.master.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ken.master")
public class MasterConfigurationProperties {
    private String token;
}
