package io.mwguy.cdnken.master.configuration;

import io.mwguy.cdnken.master.configuration.properties.MasterConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(MasterConfigurationProperties.class)
public class MasterConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(@NonNull ClientHttpResponse response) {
                // ignore
            }
        });

        return restTemplate;
    }
}
